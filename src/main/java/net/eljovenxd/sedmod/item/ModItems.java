package net.eljovenxd.sedmod.item;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.item.custom.FuelItem;
import net.eljovenxd.sedmod.item.custom.MetalDetectorItem;
import net.eljovenxd.sedmod.item.custom.ModFoods;
import net.eljovenxd.sedmod.sounds.ModSounds; // --- AÑADE ESTA IMPORTACIÓN ---
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SedMod.MOD_ID);

    public static final RegistryObject<Item> COCA = ITEMS.register("coca",
            () -> new DrinkItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.1f)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200), 1.0F)
                    .alwaysEat()
                    .build()),
                    ModSounds.ERUPTO.get() // --- AÑADE ESTE PARÁMETRO ---
            ));

    public static final RegistryObject<Item> PEPSI = ITEMS.register("pepsi",
            () -> new DrinkItem(new Item.Properties().food(new FoodProperties.Builder() // Este no tendrá sonido
                    .nutrition(1).saturationMod(0.1f)
                    .alwaysEat()
                    .build())));

    public static final RegistryObject<Item> AGUA = ITEMS.register("agua",
            () -> new DrinkItem(new Item.Properties().food(new FoodProperties.Builder() // Este tampoco
                    .nutrition(2).saturationMod(0.3f)
                    .alwaysEat()
                    .build())));

    public static final RegistryObject<Item> LATA_COMBUSTIBLE = ITEMS.register("lata_combustible",
            () -> new FuelItem(new Item.Properties(), 5000));

    public static final RegistryObject<Item> MARUCHAN = ITEMS.register("maruchan",
            () -> new Item(new Item.Properties().food(ModFoods.MARUCHAN)));

    public static final RegistryObject<Item> RAW_PLASTICO = ITEMS.register("raw_plastico",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_ALUMINIO = ITEMS.register("raw_aluminio",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PLASTICO_INGOT = ITEMS.register("plastico_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALUMINIO_INGOT = ITEMS.register("aluminio_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));

    //Herramientas de aluminio
    public static final RegistryObject<Item> ALUMINIO_SWORD = ITEMS.register("aluminio_sword",
            () -> new SwordItem(ModToolTiers.ALUMINIO, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> ALUMINIO_PICKAXE = ITEMS.register("aluminio_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ALUMINIO, 1, 1, new Item.Properties()));

    public static final RegistryObject<Item> ALUMINIO_AXE = ITEMS.register("aluminio_axe",
            () -> new AxeItem(ModToolTiers.ALUMINIO, 7, 1, new Item.Properties()));

    public static final RegistryObject<Item> ALUMINIO_SHOVEL = ITEMS.register("aluminio_shovel",
            () -> new ShovelItem(ModToolTiers.ALUMINIO, 0, 0, new Item.Properties()));

    public static final RegistryObject<Item> ALUMINIO_HOE = ITEMS.register("aluminio_hoe",
            () -> new HoeItem(ModToolTiers.ALUMINIO, 0, 0, new Item.Properties()));

    //Herramientas de plastico
    public static final RegistryObject<Item> PLASTICO_SWORD = ITEMS.register("plastico_sword",
            () -> new SwordItem(ModToolTiers.PLASTICO, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> PLASTICO_PICKAXE = ITEMS.register("plastico_pickaxe",
            () -> new PickaxeItem(ModToolTiers.PLASTICO, 1, 1, new Item.Properties()));

    public static final RegistryObject<Item> PLASTICO_AXE = ITEMS.register("plastico_axe",
            () -> new AxeItem(ModToolTiers.PLASTICO, 7, 1, new Item.Properties()));

    public static final RegistryObject<Item> PLASTICO_SHOVEL = ITEMS.register("plastico_shovel",
            () -> new ShovelItem(ModToolTiers.PLASTICO, 0, 0, new Item.Properties()));

    public static final RegistryObject<Item> PLASTICO_HOE = ITEMS.register("plastico_hoe",
            () -> new HoeItem(ModToolTiers.PLASTICO, 0, 0, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}