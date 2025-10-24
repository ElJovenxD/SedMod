package net.eljovenxd.sedmod.ModEvents;
import java.util.Random;
import net.eljovenxd.sedmod.fatigue.FatigueProvider;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.fatigue.IFatigue;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
import net.eljovenxd.sedmod.sounds.ModSounds;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.EntityType; // --- NUEVA IMPORTACIÓN ---
import net.minecraft.world.entity.monster.Creeper; // --- NUEVA IMPORTACIÓN ---
import net.minecraft.world.entity.monster.Zombie; // --- NUEVA IMPORTACIÓN ---
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent; // --- NUEVA IMPORTACIÓN ---
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.ExplosionEvent; // --- NUEVA IMPORTACIÓN ---
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

@Mod.EventBusSubscriber(modid = "sedmod")
public class ModEvents {

    // --- NUEVA CONSTANTE ---
    private static final String HALLUCINATION_TAG = "SedModHallucination_Timer";

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
                    newStore.setLastSleepTime(oldStore.getLastSleepTime());
                    newStore.setSleeping(oldStore.isSleeping());
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
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
            });
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTrySleep(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();

        // Si es de día y no hay tormenta (condición para la siesta)
        if (!player.level().isNight() && !player.level().isThundering()) {

            // --- NUEVA REVISIÓN ---
            // Revisa si hay monstruos hostiles cerca (lógica copiada de la clase Player)
            if (!player.level().getEntitiesOfClass(net.minecraft.world.entity.monster.Monster.class,
                    player.getBoundingBox().inflate(8.0D, 5.0D, 8.0D),
                    (monster) -> monster.isPreventingPlayerRest(player)).isEmpty())
            {
                // Si hay monstruos, no dejes dormir y pon el resultado de vanilla
                event.setResult(Player.BedSleepingProblem.NOT_SAFE);
            } else {
                // Si está despejado, permite la siesta
                event.setResult(PlayerSleepInBedEvent.Result.ALLOW);
                player.startSleeping(event.getPos());
            }
            // --- FIN DE LA REVISIÓN ---
        }
        // Si es de noche, no hacemos nada y dejamos que Minecraft maneje el sueño normal.
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                fatigue.setSleeping(false);

                long startTime = fatigue.getLastSleepTime();
                long worldTime = player.level().getDayTime() % 24000;
                boolean didSkipNight = worldTime < startTime;

                if (!didSkipNight) {
                    player.level().playSound(null, player.blockPosition(), ModSounds.BOSTEZO.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.setDayTime(serverLevel.getDayTime() + 2000);
                    }

                    player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                        thirst.removeThirst(1);
                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
                    });

                    fatigue.addFatigue(4);
                    ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);

                    return;
                }

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

                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
        }
    }

    // --- CAMBIO GRANDE: Lógica de Tick ---
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            Player player = event.player;
            ServerLevel level = (ServerLevel) player.level(); // --- VARIABLE NECESARIA ---

            // --- LÓGICA DE ALUCINACIONES (Cada tick) ---
            // 1. Actualizar temporizador de alucinaciones existentes y eliminarlas si expiran
            level.getEntities(EntityType.ZOMBIE, player.getBoundingBox().inflate(64.0), (e) -> e.getPersistentData().contains(HALLUCINATION_TAG))
                    .forEach(zombie -> tickHallucination(zombie));
            level.getEntities(EntityType.CREEPER, player.getBoundingBox().inflate(64.0), (e) -> e.getPersistentData().contains(HALLUCINATION_TAG))
                    .forEach(creeper -> tickHallucination(creeper));
            // --- FIN LÓGICA CADA TICK ---

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

            // --- LÓGICA DE FATIGA (AJUSTADA A 3000 TICKS) ---
            if (player.level().getGameTime() % 3000 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    if (fatigue.getFatigue() > 0) {
                        fatigue.removeFatigue(1);
                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), (ServerPlayer) player);
                    }
                });
            }

            // --- LÓGICA DE EFECTOS POR FATIGA (Y ALUCINACIONES) ---
            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    int fatigueLevel = fatigue.getFatigue();

                    if (fatigueLevel <= 0 && !player.isSleeping() && !player.isUsingItem()) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }

                    else if (fatigueLevel <= 6) {
                        // Efectos de lentitud
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false, true));

                        // --- 2. Probabilidad de invocar alucinación ---
                        // Una probabilidad de 1 en 10 cada 4 segundos (80 ticks)
                        if (level.random.nextInt(10) == 0) { // <--- ¡AQUÍ ESTÁ EL CAMBIO!
                            spawnHallucination(player);
                        }
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

    // --- NUEVO MÉTODO PRIVADO ---
    private static void tickHallucination(Entity entity) {
        int timer = entity.getPersistentData().getInt(HALLUCINATION_TAG);
        if (timer <= 1) {
            entity.discard(); // Elimina la entidad del mundo
        } else {
            entity.getPersistentData().putInt(HALLUCINATION_TAG, timer - 1);
        }
    }

    // --- NUEVO MÉTODO PRIVADO (CORREGIDO) ---
    private static void spawnHallucination(Player player) {
        ServerLevel level = (ServerLevel) player.level();
        // Random random = (Random) level.random; // <-- LÍNEA ELIMINADA

        // Posición aleatoria cerca del jugador (entre 5 y 15 bloques de distancia)
        double angle = level.random.nextDouble() * 2 * Math.PI; // <-- CAMBIO
        double distance = 5 + level.random.nextDouble() * 10; // <-- CAMBIO
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;
        double y = player.getY() + 1; // Un poco arriba para que no se atore

        // 50% chance de Creeper, 50% de Zombie
        if (level.random.nextBoolean()) { // <-- CAMBIO
            Creeper creeper = new Creeper(EntityType.CREEPER, level);
            creeper.setPos(x, y, z);
            // Marcar como alucinación por 5 segundos (100 ticks)
            creeper.getPersistentData().putInt(HALLUCINATION_TAG, 100);
            level.addFreshEntity(creeper);
        } else {
            Zombie zombie = new Zombie(EntityType.ZOMBIE, level);
            zombie.setPos(x, y, z);
            // Marcar como alucinación por 10 segundos (200 ticks)
            zombie.getPersistentData().putInt(HALLUCINATION_TAG, 200);

            // --- ¡EL TRUCO DE HEROBRINE! ---
            // Quita su IA y hace que te mire fijamente
            zombie.getBrain().removeAllBehaviors();
            zombie.getLookControl().setLookAt(player, 180.0F, 180.0F);
            // ---------------------------------

            level.addFreshEntity(zombie);
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

    // --- NUEVO MÉTODO DE EVENTO ---
    @SubscribeEvent
    public static void onEntityAttack(LivingHurtEvent event) {
        // Si la entidad que ataca (getSource().getEntity()) es un mob...
        if (event.getSource().getEntity() instanceof Zombie || event.getSource().getEntity() instanceof Creeper) {
            // ...y tiene nuestra etiqueta de alucinación...
            if (event.getSource().getEntity().getPersistentData().contains(HALLUCINATION_TAG)) {
                // ...cancela el daño.
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Start event) {
        // Comprueba si la entidad que explota (getExploder()) es un Creeper...
        if (event.getExplosion().getExploder() instanceof Creeper creeper) { // <-- ¡ESTA LÍNEA ES LA CORRECCIÓN!

            // ...y tiene nuestra etiqueta de alucinación...
            if (creeper.getPersistentData().contains(HALLUCINATION_TAG)) {
                // ...cancela la explosión (ni daño, ni agujeros).
                event.setCanceled(true);

                // (Opcional) Reproduce el sonido de explosión de todos modos para asustar
                creeper.level().playSound(null, creeper.getX(), creeper.getY(), creeper.getZ(),
                        net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                        net.minecraft.sounds.SoundSource.HOSTILE, 4.0F, 1.0F);
            }
        }
    }
}