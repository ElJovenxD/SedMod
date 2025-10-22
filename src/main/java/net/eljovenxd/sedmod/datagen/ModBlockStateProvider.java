package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SedMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Genera modelos simples (misma textura en todas las caras)
        blockWithItem(ModBlocks.SOUND_BLOCK);
        blockWithItem(ModBlocks.PLASTICO_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_PLASTICO_ORE);
        blockWithItem(ModBlocks.ALUMINIO_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_ALUMINIO_ORE);

        // Genera modelos para las cajas (que tienen texturas diferentes)
        // Asume que los modelos JSON (caja_coca.json, etc.) ya existen y están correctos
        simpleBlockWithItem(ModBlocks.CAJA_COCA.get(),
                models().cube("caja_coca",
                        modLoc("block/coca_abajo"), // down
                        modLoc("block/coca_arriba"), // up
                        modLoc("block/coca_lado"), // north
                        modLoc("block/coca_lado"), // south
                        modLoc("block/coca_lado"), // east
                        modLoc("block/coca_lado")) // west
        );

        simpleBlockWithItem(ModBlocks.CAJA_PEPSI.get(),
                models().cube("caja_pepsi",
                        modLoc("block/pepsi_abajo"), // down
                        modLoc("block/pepsi_arriba"), // up
                        modLoc("block/pepsi_lado"), // north
                        modLoc("block/pepsi_lado"), // south
                        modLoc("block/pepsi_lado"), // east
                        modLoc("block/pepsi_lado")) // west
        );
    }

    // Helper para bloques con item de bloque (la mayoría)
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}