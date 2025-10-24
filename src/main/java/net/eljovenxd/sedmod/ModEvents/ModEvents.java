package net.eljovenxd.sedmod.ModEvents;

import net.eljovenxd.sedmod.fatigue.FatigueProvider;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.fatigue.IFatigue;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
import net.eljovenxd.sedmod.sounds.ModSounds; // --- CAMBIO (Asegurarse que esté importado) ---
import net.minecraft.sounds.SoundSource; // --- CAMBIO (Asegurarse que esté importado) ---
// --- CAMBIO (Import necesario para avanzar el tiempo) ---
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstProvider;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;

@Mod.EventBusSubscriber(modid = "sedmod")
public class ModEvents {

    // --- SIN CAMBIOS ---
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ThirstStorage.THIRST).isPresent()) {
                event.addCapability(new ResourceLocation("sedmod", "thirst"), new ThirstProvider());
            }
            if (!event.getObject().getCapability(FatigueStorage.FATIGUE).isPresent()) {
                event.addCapability(new ResourceLocation("sedmod", "fatigue"), new FatigueProvider());
            }
        }
    }

    // --- SIN CAMBIOS ---
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ThirstStorage.THIRST).ifPresent(oldStore -> {
                event.getEntity().getCapability(ThirstStorage.THIRST).ifPresent(newStore -> {
                    newStore.setThirst(oldStore.getThirst());
                    newStore.setThirstSaturation(oldStore.getThirstSaturation());
                });
            });
            event.getOriginal().getCapability(FatigueStorage.FATIGUE).ifPresent(oldStore -> {
                event.getEntity().getCapability(FatigueStorage.FATIGUE).ifPresent(newStore -> {
                    newStore.setFatigue(oldStore.getFatigue());
                    newStore.setLastSleepTime(oldStore.getLastSleepTime());
                    newStore.setSleeping(oldStore.isSleeping());
                });
            });
        }
    }

    // --- SIN CAMBIOS ---
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ThirstStorage.register(event);
        FatigueStorage.register(event);
    }

    // --- SIN CAMBIOS ---
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
            });
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }

    // --- SIN CAMBIOS (Tu código aquí estaba correcto) ---
    @SubscribeEvent
    public static void onPlayerTrySleep(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();
        if (!player.level().isNight() && !player.level().isThundering()) {
            event.setResult(PlayerSleepInBedEvent.Result.ALLOW);
            player.startSleeping(event.getPos());
        }
    }


    // --- CAMBIO GRANDE: Lógica de despertar ---
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                fatigue.setSleeping(false);

                long startTime = fatigue.getLastSleepTime();
                long worldTime = player.level().getDayTime() % 24000;
                boolean didSkipNight = worldTime < startTime;

                // --- MECÁNICA: Siesta de día ---
                if (!didSkipNight) {

                    // 1. Sonido de Bostezo (como pediste)
                    player.level().playSound(null, player.blockPosition(), ModSounds.BOSTEZO.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    // 2. Avanzar el tiempo 2 horas (2000 ticks)
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.setDayTime(serverLevel.getDayTime() + 2000);
                    }

                    // 3. Quitar Sed proporcional (1 punto)
                    player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                        thirst.removeThirst(1);
                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
                    });

                    // 4. Añadir Fatiga proporcional (4 puntos = 2 lunas)
                    fatigue.addFatigue(4);
                    // 5. Arreglar bug visual: Enviar paquete de fatiga AHORA
                    ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);

                    return; // Termina la siesta
                }

                // --- MECÁNICA: Dormir de Noche (Lógica anterior) ---

                // Mover la pérdida de sed aquí, solo si duerme de noche
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.removeThirst(6);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
                });

                final long NIGHT_START_TICK = 12542;
                final long NIGHT_DURATION_TICKS = 24000 - NIGHT_START_TICK;

                long clampedStartTime = Math.max(startTime, NIGHT_START_TICK);
                long ticksSlept = 24000 - clampedStartTime;

                double percentSlept = (double)ticksSlept / NIGHT_DURATION_TICKS;
                int effectDuration = 24000;

                if (percentSlept >= 0.5) {
                    fatigue.setFatigue(20);
                }

                if (percentSlept >= 0.9) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, effectDuration, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectDuration, 0));
                } else if (percentSlept >= 0.7) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, effectDuration, 0));
                }

                // Enviar paquete de fatiga al despertar
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }
    // --- FIN DEL CAMBIO GRANDE ---


    // --- CAMBIO GRANDE: Lógica de Tick ---
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            Player player = event.player;

            // --- CAMBIO: ELIMINADO EL BLOQUE if(player.isSleeping()) ---
            // La lógica de "player.sleepCounter = 0" y la regeneración lenta
            // se eliminaron porque eran incompatibles con avanzar el tiempo.
            // Ahora la regeneración es instantánea al despertar (en onPlayerWakeUp).

            // --- Lógica de Sed (SIN CAMBIOS) ---
            if (player.level().getGameTime() % 600 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() > 0) {
                        if (thirst.getThirstSaturation() > 0) {
                            thirst.setThirstSaturation(thirst.getThirstSaturation() - 1.0F);
                        } else {
                            thirst.removeThirst(1);
                        }
                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                    }
                });
            }
            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() <= 0) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }
                });
            }

            // --- LÓGICA DE FATIGA (SIN CAMBIOS) ---
            if (player.level().getGameTime() % 400 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    if (fatigue.getFatigue() > 0) {
                        fatigue.removeFatigue(1);
                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), (ServerPlayer) player);
                    }
                });
            }

            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    int fatigueLevel = fatigue.getFatigue();

                    if (fatigueLevel <= 0 && !player.isSleeping() && !player.isUsingItem()) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }

                    else if (fatigueLevel <= 6) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false, true));
                    }
                });
            }

            // --- LÓGICA DE DETECTAR SUEÑO (SIN CAMBIOS) ---
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                if (player.isSleeping() && !fatigue.isSleeping()) {
                    fatigue.setSleeping(true);
                    fatigue.setLastSleepTime(player.level().getDayTime() % 24000);
                }
            });
        }
    }
    // --- FIN DEL CAMBIO GRANDE ---


    // --- SIN CAMBIOS ---
    @SubscribeEvent
    public static void onPlayerFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack itemStack = event.getItem();

            if (itemStack.is(ModItems.AGUA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(5);
                    thirst.addThirstSaturation(6.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.COCA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(15);
                    thirst.addThirstSaturation(18.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.PEPSI.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(10);
                    thirst.addThirstSaturation(12.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                });
            }
            else if (itemStack.is(Items.POTION) && PotionUtils.getPotion(itemStack) == Potions.WATER) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(3);
                    thirst.addThirstSaturation(3.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                });
            }
        }
    }
}