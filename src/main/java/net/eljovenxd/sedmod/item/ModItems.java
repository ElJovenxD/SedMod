package net.eljovenxd.sedmod.item;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.item.custom.FuelItem;
import net.eljovenxd.sedmod.item.custom.MetalDetectorItem;
import net.eljovenxd.sedmod.item.custom.ModFoods;
import net.eljovenxd.sedmod.sounds.ModSounds; // --- AÑADE ESTA IMPORTACIÓN ---
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
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

    // ... (El resto de tu archivo sigue igual) ...

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


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}