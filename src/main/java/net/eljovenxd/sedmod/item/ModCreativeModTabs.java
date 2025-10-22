package net.eljovenxd.sedmod.item;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SedMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COCA.get()))
                    .title(Component.translatable("creativetab.tutorial_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // Items
                        pOutput.accept(ModItems.COCA.get());
                        pOutput.accept(ModItems.PEPSI.get());
                        pOutput.accept(ModItems.AGUA.get());
                        pOutput.accept(ModItems.METAL_DETECTOR.get());
                        pOutput.accept(ModItems.MARUCHAN.get());
                        pOutput.accept(ModItems.LATA_COMBUSTIBLE.get());
                        pOutput.accept(ModItems.RAW_ALUMINIO.get());
                        pOutput.accept(ModItems.ALUMINIO_INGOT.get());

                        // Resources
                        pOutput.accept(ModItems.PLASTICO_INGOT.get());
                        pOutput.accept(ModItems.ALUMINIO_INGOT.get());
                        pOutput.accept(ModItems.RAW_PLASTICO.get());
                        pOutput.accept(ModItems.RAW_ALUMINIO.get());

                        // Blocks
                        pOutput.accept(ModBlocks.CAJA_COCA.get());
                        pOutput.accept(ModBlocks.CAJA_PEPSI.get());
                        pOutput.accept(ModBlocks.PLASTICO_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_PLASTICO_ORE.get());
                        pOutput.accept(ModBlocks.ALUMINIO_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());
                        pOutput.accept(ModBlocks.SOUND_BLOCK.get());
                        pOutput.accept(ModBlocks.ALUMINIO_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
