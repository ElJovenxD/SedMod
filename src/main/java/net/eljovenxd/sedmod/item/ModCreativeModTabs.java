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
                    .title(Component.translatable("creativetab.tutorial_tab")) // Asegúrate que 'creativetab.tutorial_tab' esté en tu en_us.json
                    .displayItems((pParameters, pOutput) -> {
                        // Items Simples
                        pOutput.accept(ModItems.COCA.get());
                        pOutput.accept(ModItems.PEPSI.get());
                        pOutput.accept(ModItems.AGUA.get());
                        pOutput.accept(ModItems.METAL_DETECTOR.get());
                        pOutput.accept(ModItems.MARUCHAN.get());
                        pOutput.accept(ModItems.LATA_COMBUSTIBLE.get());
                        pOutput.accept(ModItems.LECHUGA.get());
                        pOutput.accept(ModItems.LECHUGA_SEEDS.get());


                        // Recursos (Lingotes y Crudos)
                        pOutput.accept(ModItems.RAW_PLASTICO.get());
                        pOutput.accept(ModItems.PLASTICO_INGOT.get());
                        pOutput.accept(ModItems.RAW_ALUMINIO.get());
                        pOutput.accept(ModItems.ALUMINIO_INGOT.get());

                        // Bloques Base y Menas
                        pOutput.accept(ModBlocks.PLASTICO_BLOCK.get());
                        pOutput.accept(ModBlocks.ALUMINIO_BLOCK.get());
                        pOutput.accept(ModBlocks.PLASTICO_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_PLASTICO_ORE.get());
                        pOutput.accept(ModBlocks.ALUMINIO_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());
                        pOutput.accept(ModBlocks.CAJA_COCA.get());
                        pOutput.accept(ModBlocks.CAJA_PEPSI.get());
                        pOutput.accept(ModBlocks.SOUND_BLOCK.get());

                        // Set de Plástico
                        pOutput.accept(ModBlocks.PLASTICO_STAIRS.get());
                        pOutput.accept(ModBlocks.PLASTICO_SLAB.get());
                        pOutput.accept(ModBlocks.PLASTICO_BUTTON.get());
                        pOutput.accept(ModBlocks.PLASTICO_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.PLASTICO_FENCE.get());
                        pOutput.accept(ModBlocks.PLASTICO_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.PLASTICO_WALL.get());
                        pOutput.accept(ModBlocks.PLASTICO_DOOR.get());
                        pOutput.accept(ModBlocks.PLASTICO_TRAPDOOR.get());

                        // Set de Aluminio
                        pOutput.accept(ModBlocks.ALUMINIO_STAIRS.get());
                        pOutput.accept(ModBlocks.ALUMINIO_SLAB.get());
                        pOutput.accept(ModBlocks.ALUMINIO_BUTTON.get());
                        pOutput.accept(ModBlocks.ALUMINIO_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.ALUMINIO_FENCE.get());
                        pOutput.accept(ModBlocks.ALUMINIO_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.ALUMINIO_WALL.get());
                        pOutput.accept(ModBlocks.ALUMINIO_DOOR.get());
                        pOutput.accept(ModBlocks.ALUMINIO_TRAPDOOR.get());

                        // Herramientas de Aluminio
                        pOutput.accept(ModItems.ALUMINIO_SWORD.get());
                        pOutput.accept(ModItems.ALUMINIO_PICKAXE.get());
                        pOutput.accept(ModItems.ALUMINIO_AXE.get());
                        pOutput.accept(ModItems.ALUMINIO_SHOVEL.get());
                        pOutput.accept(ModItems.ALUMINIO_HOE.get());

                        // Herramientas de Plástico
                        pOutput.accept(ModItems.PLASTICO_SWORD.get());
                        pOutput.accept(ModItems.PLASTICO_PICKAXE.get());
                        pOutput.accept(ModItems.PLASTICO_AXE.get());
                        pOutput.accept(ModItems.PLASTICO_SHOVEL.get());
                        pOutput.accept(ModItems.PLASTICO_HOE.get());
                        pOutput.accept(ModItems.PIEDRITA.get());
                        pOutput.accept(ModItems.PLASTICO_CUCHILLO.get());
                        pOutput.accept(ModItems.ALUMINIO_CUCHILLO.get());

                        // Armadura de Aluminio
                        pOutput.accept(ModItems.ALUMINIO_HELMET.get());
                        pOutput.accept(ModItems.ALUMINIO_CHESTPLATE.get());
                        pOutput.accept(ModItems.ALUMINIO_LEGGINGS.get());
                        pOutput.accept(ModItems.ALUMINIO_BOOTS.get());

                        // Armadura de Plástico
                        pOutput.accept(ModItems.PLASTICO_HELMET.get());
                        pOutput.accept(ModItems.PLASTICO_CHESTPLATE.get());
                        pOutput.accept(ModItems.PLASTICO_LEGGINGS.get());
                        pOutput.accept(ModItems.PLASTICO_BOOTS.get());

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}