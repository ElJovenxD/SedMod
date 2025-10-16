package net.eljovenxd.sedmod.item;

import net.eljovenxd.sedmod.SedMod;
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
                    .alwaysEat() // <-- AÑADE ESTA LÍNEA
                    .build())));

    public static final RegistryObject<Item> PEPSI = ITEMS.register("pepsi",
            () -> new DrinkItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.1f)
                    .alwaysEat() // <-- AÑADE ESTA LÍNEA
                    .build())));

    public static final RegistryObject<Item> AGUA = ITEMS.register("agua",
            () -> new DrinkItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f)
                    .alwaysEat() // <-- AÑADE ESTA LÍNEA
                    .build())));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}