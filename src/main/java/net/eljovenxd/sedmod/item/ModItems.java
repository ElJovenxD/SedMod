package net.eljovenxd.sedmod.item;

import net.eljovenxd.sedmod.SedMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SedMod.MOD_ID);

    public static final RegistryObject<Item> COCA = ITEMS.register("coca",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEPSI = ITEMS.register("pepsi",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AGUA = ITEMS.register("agua",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
