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
        blockWithItem(ModBlocks.ALUMINIO_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_ALUMINIO_ORE);
        blockWithItem(ModBlocks.PLASTICO_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_PLASTICO_ORE);

        blockWithItem(ModBlocks.SOUND_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}