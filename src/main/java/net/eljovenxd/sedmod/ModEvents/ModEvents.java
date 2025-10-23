package net.eljovenxd.sedmod.ModEvents;

import net.eljovenxd.sedmod.fatigue.FatigueProvider;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.fatigue.IFatigue;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
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

@Mod.EventBusSubscriber(modid = "sedmod")
public class ModEvents {

    // ... (onAttachCapabilitiesPlayer, onPlayerCloned, onRegisterCapabilities, onPlayerLoggedIn se quedan igual) ...
    // ... (No es necesario copiarlos de nuevo si no cambiaron) ...

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
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ThirstStorage.register(event);
        FatigueStorage.register(event);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
            });
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }


    // --- EVENTO MODIFICADO (CORRECCIÓN 1) ---
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                thirst.removeThirst(6);
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
            });

            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                fatigue.setSleeping(false);

                long startTime = fatigue.getLastSleepTime();
                long worldTime = player.level().getDayTime() % 24000;
                boolean didSkipNight = worldTime < startTime;

                // --- MECÁNICA 4: Dormir de día o ser despertado ---
                if (!didSkipNight) {
                    fatigue.setFatigue(20);
                    // --- CORRECCIÓN 1: ESTE ERA EL PAQUETE INCORRECTO ---
                    ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
                    return;
                }

                // --- LÓGICA DE BONIFICACIÓN POR NOCHE ---
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

                // Sincroniza la fatiga (si no se hizo ya en el bloque de "dormir de día")
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }

    // --- EVENTO MODIFICADO (CORRECCIÓN 2) ---
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            Player player = event.player;

            // --- Lógica de Sed (Existente) ---
            if (player.level().getGameTime() % 600 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() > 0) {
                        if (thirst.getThirstSaturation() > 0) {
                            thirst.setThirstSaturation(thirst.getThirstSaturation() - 1.0F);
                        } else {
                            thirst.removeThirst(1);
                        }
                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
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

            // --- LÓGICA DE FATIGA (MODIFICADA) ---

            // Lógica #3: Reducir la fatiga lentamente (Se queda igual)
            if (player.level().getGameTime() % 400 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    if (fatigue.getFatigue() > 0) {
                        fatigue.removeFatigue(1);
                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), (ServerPlayer) player);
                    }
                });
            }

            // Lógica #4: Aplicar efectos y daño por fatiga (Se queda igual)
            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    int fatigueLevel = fatigue.getFatigue();

                    // --- CORRECCIÓN 2: NO HACER DAÑO SI EL JUGADOR INTENTA DORMIR O BEBER ---
                    // Esto soluciona los problemas 1 y 2.
                    if (fatigueLevel <= 0 && !player.isSleeping() && !player.isUsingItem()) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }
                    // --- FIN CORRECCIÓN 2 ---

                    else if (fatigueLevel <= 6) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false, true));
                    }
                });
            }

            // LÓGICA #5: DETECTAR INICIO DE SUEÑO (Se queda igual)
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                if (player.isSleeping() && !fatigue.isSleeping()) {
                    fatigue.setSleeping(true);
                    fatigue.setLastSleepTime(player.level().getDayTime() % 24000);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack itemStack = event.getItem();

            if (itemStack.is(ModItems.AGUA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(5);
                    thirst.addThirstSaturation(6.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.COCA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(15);
                    thirst.addThirstSaturation(18.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.PEPSI.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(10);
                    thirst.addThirstSaturation(12.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            }
            else if (itemStack.is(Items.POTION) && PotionUtils.getPotion(itemStack) == Potions.WATER) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(3);
                    thirst.addThirstSaturation(3.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            }
        }
    }
}