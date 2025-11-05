package net.eljovenxd.sedmod.ModEvents;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
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
import net.eljovenxd.sedmod.villager.ModVillagers;
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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

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

        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            if(event.getType() == VillagerProfession.FARMER) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

                // Level 1
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ModItems.LECHUGA.get(), 12),
                        10, 8, 0.02f));

                // Level 2
                trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.IRON_INGOT, 3),
                        new ItemStack(ModItems.LECHUGA_SEEDS.get(), 6),
                        5, 9, 0.035f));


            if(event.getType() == VillagerProfession.LIBRARIAN) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades1 = event.getTrades();
                ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.MENDING, 1));

                // Level 1
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 32),
                        enchantedBook,
                        2, 8, 0.02f));
            }

            if(event.getType() == VillagerProfession.ARMORER){
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades2 = event.getTrades();

                // Level 1
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.PLASTICO_INGOT.get(),6),
                        new ItemStack(Items.EMERALD, 1),
                        15, 8, 0.02f));

                // Level 2
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.ALUMINIO_INGOT.get(),4),
                        new ItemStack(Items.EMERALD, 1),
                        15, 8, 0.02f));
            }

            if(event.getType() == ModVillagers.COCA_MASTER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades2 = event.getTrades();

                //Level 1
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.COAL, 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        16, 1, 0.05f));
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.COPPER_INGOT, 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        16, 1, 0.05f));
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.PLASTICO_INGOT.get(), 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        16, 1, 0.05f));

                //Level 2
                trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.IRON_INGOT, 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        16, 2, 0.05f));
                trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.ALUMINIO_INGOT.get(), 4),
                        new ItemStack(ModItems.COCA.get(), 1),
                        16, 2, 0.05f));

                //Level 3
                trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.GOLD_INGOT, 1),
                        new ItemStack(ModItems.COCA.get(), 3),
                        12, 3, 0.05f));
                trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.LAPIS_LAZULI, 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        12, 3, 0.05f));
                trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.REDSTONE, 8),
                        new ItemStack(ModItems.COCA.get(), 1),
                        12, 3, 0.05f));

                //Level 4
                trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.DIAMOND, 1),
                        new ItemStack(ModItems.COCA.get(), 8),
                        8, 5, 0.05f));
                trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        new ItemStack(ModItems.COCA.get(), 8),
                        16, 3, 0.05f));

                //Level 5
                trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.DIAMOND, 2),
                        new ItemStack(ModBlocks.CAJA_COCA.get(), 3),
                        3, 10, 0.05f));
                //Level 6
                trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.COBBLESTONE, 16),
                        new ItemStack(ModItems.COCA.get(), 2),
                        4, 8, 0.05f));
            }

                if(event.getType() == ModVillagers.PEPSI_MASTER.get()) {
                    Int2ObjectMap<List<VillagerTrades.ItemListing>> trades2 = event.getTrades();

                    //Level 1
                    trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.COAL, 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            16, 1, 0.05f));
                    trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.COPPER_INGOT, 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            16, 1, 0.05f));
                    trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(ModItems.PLASTICO_INGOT.get(), 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            16, 1, 0.05f));

                    //Level 2
                    trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.IRON_INGOT, 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            16, 2, 0.05f));
                    trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(ModItems.ALUMINIO_INGOT.get(), 4),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            16, 2, 0.05f));

                    //Level 3
                    trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.GOLD_INGOT, 1),
                            new ItemStack(ModItems.PEPSI.get(), 3),
                            12, 3, 0.05f));
                    trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.LAPIS_LAZULI, 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            12, 3, 0.05f));
                    trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.REDSTONE, 8),
                            new ItemStack(ModItems.PEPSI.get(), 1),
                            12, 3, 0.05f));

                    //Level 4
                    trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.DIAMOND, 1),
                            new ItemStack(ModItems.PEPSI.get(), 8),
                            8, 5, 0.05f));
                    trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(ModItems.PEPSI.get(), 8),
                            16, 3, 0.05f));

                    //Level 5
                    trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.DIAMOND, 2),
                            new ItemStack(ModBlocks.CAJA_PEPSI.get(), 3),
                            3, 10, 0.05f));
                    //Level 6
                    trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                            new ItemStack(Items.COBBLESTONE, 16),
                            new ItemStack(ModItems.PEPSI.get(), 2),
                            4, 8, 0.05f));
                }
        }
    }

    @SubscribeEvent
    public static void addCustomWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 1),
                new ItemStack(ModItems.CEMPASUCHIL.get(), 1),
                3, 2, 0.2f));

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 2),
                new ItemStack(ModItems.METAL_DETECTOR.get(), 1),
                2, 12, 0.15f));
    }
}
