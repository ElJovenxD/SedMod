package net.eljovenxd.sedmod.ModEvents;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.fatigue.FatigueProvider;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstProvider;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.eljovenxd.sedmod.cocacounter.CocaCounterProvider;
import net.eljovenxd.sedmod.cocacounter.ICocaCounter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SedMod.MOD_ID)
public class ModEvents {

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
            if (!event.getObject().getCapability(CocaCounterProvider.COCA_COUNTER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(SedMod.MOD_ID, "coca_counter"), new CocaCounterProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player newPlayer = event.getEntity();
            Player oldPlayer = event.getOriginal();

            AttributeInstance maxHealthAttribute = newPlayer.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttribute != null) {
                maxHealthAttribute.setBaseValue(20.0D);
            }

            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                if (newPlayer.getInventory().getItem(i).is(ModItems.PIEDRITA.get())) {
                    newPlayer.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }

            oldPlayer.getCapability(ThirstStorage.THIRST).ifPresent(oldStore ->
                    newPlayer.getCapability(ThirstStorage.THIRST).ifPresent(newStore -> {
                        newStore.setThirst(20);
                        newStore.setThirstSaturation(5.0F);
                    })
            );
            oldPlayer.getCapability(FatigueStorage.FATIGUE).ifPresent(oldStore ->
                    newPlayer.getCapability(FatigueStorage.FATIGUE).ifPresent(newStore -> {
                        newStore.setFatigue(20);
                    })
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ThirstStorage.register(event);
        FatigueStorage.register(event);
        event.register(ICocaCounter.class);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemToss(ItemTossEvent event) {
        ItemStack tossedStack = event.getEntity().getItem();
        if (tossedStack.is(ModItems.PIEDRITA.get())) {
            event.setCanceled(true);
            Player player = event.getPlayer();
            if (!player.getInventory().add(tossedStack.copy())) {
                 player.drop(tossedStack.copy(), false);
            }
            if (!player.level().isClientSide) {
                player.sendSystemMessage(Component.translatable("message.sedmod.piedrita_stuck_toss"));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            if (player.isCreative()) {
                return;
            }

            // --- BLOQUEO DE PIEDRITA EN CURSOR Y OTROS INVENTARIOS ---
            handlePiedritaInCursor(player);
            handlePiedritaInOpenContainer(player);

            // --- EFECTOS DE SED Y FATIGA ---
            if (event.player.level().getGameTime() % 600 == 0 && !player.isSpectator()) {
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

            if (event.player.level().getGameTime() % 80 == 0 && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() <= 0) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }
                });
            }

            if (event.player.level().getGameTime() % 3000 == 0 && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    if (fatigue.getFatigue() > 0) {
                        fatigue.removeFatigue(1);
                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), (ServerPlayer) player);
                    }
                });
            }

            if (event.player.level().getGameTime() % 80 == 0 && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    int fatigueLevel = fatigue.getFatigue();
                    if (fatigueLevel <= 0 && !player.isSleeping() && !player.isUsingItem()) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    } else if (fatigueLevel <= 6) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false, true));

                        if (player.level() instanceof ServerLevel level && level.random.nextInt(10) == 0) {
                            spawnHallucination(player);
                        }
                    }
                });
            }
        }
    }

    private static void handlePiedritaInCursor(Player player) {
        if (player.containerMenu.getCarried().is(ModItems.PIEDRITA.get())) {
            ItemStack piedritaStack = player.containerMenu.getCarried().copy();
            player.containerMenu.setCarried(ItemStack.EMPTY);
            if (!player.getInventory().add(piedritaStack)) {
                player.drop(piedritaStack, false);
            }
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).sendSystemMessage(Component.translatable("message.sedmod.piedrita_stuck_move"));
            }
        }
    }

    private static void handlePiedritaInOpenContainer(Player player) {
        if (player.containerMenu != player.inventoryMenu) {
            for (int i = 0; i < player.containerMenu.slots.size() - 36; i++) {
                Slot slot = player.containerMenu.getSlot(i);
                if (slot.hasItem() && slot.getItem().is(ModItems.PIEDRITA.get())) {
                    ItemStack piedritaStack = slot.getItem().copy();
                    slot.set(ItemStack.EMPTY);
                    if (!player.getInventory().add(piedritaStack)) {
                        player.drop(piedritaStack, false);
                    }
                    if (player instanceof ServerPlayer) {
                        ((ServerPlayer) player).sendSystemMessage(Component.translatable("message.sedmod.piedrita_stuck_move"));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            ServerLevel level = (ServerLevel) event.level;
            for (Entity entity : level.getAllEntities()) {
                if (entity.getPersistentData().contains(HALLUCINATION_TAG)) {
                    int timer = entity.getPersistentData().getInt(HALLUCINATION_TAG);
                    if (timer <= 1) {
                        entity.discard();
                    } else {
                        entity.getPersistentData().putInt(HALLUCINATION_TAG, timer - 1);
                    }
                }
            }
        }
    }

    private static void spawnHallucination(Player player) {
        ServerLevel level = (ServerLevel) player.level();
        int numberOfMobs = level.random.nextInt(5) + 1;

        for (int i = 0; i < numberOfMobs; i++) {
            double angle = level.random.nextDouble() * 2 * Math.PI;
            double distance = 5 + level.random.nextDouble() * 10;
            double x = player.getX() + Math.cos(angle) * distance;
            double z = player.getZ() + Math.sin(angle) * distance;
            double y = player.getY() + 1;

            if (level.random.nextBoolean()) {
                Creeper creeper = new Creeper(EntityType.CREEPER, level);
                creeper.setPos(x, y, z);
                creeper.getPersistentData().putInt(HALLUCINATION_TAG, 100);
                level.addFreshEntity(creeper);
            } else {
                Zombie zombie = new Zombie(EntityType.ZOMBIE, level);
                zombie.setPos(x, y, z);
                zombie.getPersistentData().putInt(HALLUCINATION_TAG, 200);
                level.addFreshEntity(zombie);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Zombie || event.getSource().getEntity() instanceof Creeper) {
            if (event.getSource().getEntity().getPersistentData().contains(HALLUCINATION_TAG)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Start event) {
        if (event.getExplosion().getExploder() instanceof Creeper creeper) {
            if (creeper.getPersistentData().contains(HALLUCINATION_TAG)) {
                event.setCanceled(true);
                creeper.level().playSound(null, creeper.getX(), creeper.getY(), creeper.getZ(),
                        net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                        SoundSource.HOSTILE, 4.0F, 1.0F);
            }
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
            } else if (itemStack.is(Items.POTION) && PotionUtils.getPotion(itemStack) == Potions.WATER) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(3);
                    thirst.addThirstSaturation(3.0F);
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), (ServerPlayer) player);
                });
            }
        }
    }
}
