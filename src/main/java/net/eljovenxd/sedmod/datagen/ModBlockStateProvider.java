package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.block.custom.LechugaCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

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
        blockWithItem(ModBlocks.PLASTICO_BLOCK);
        blockWithItem(ModBlocks.ALUMINIO_BLOCK);

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

        simpleBlockWithItem(ModBlocks.CEMPASUCHIL.get(), models().cross(blockTexture(ModBlocks.CEMPASUCHIL.get()).getPath(),
                blockTexture(ModBlocks.CEMPASUCHIL.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_CEMPASUCHIL.get(), models().singleTexture("potted_cempasuchil", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.CEMPASUCHIL.get())).renderType("cutout"));



        stairsBlock(((StairBlock) ModBlocks.PLASTICO_STAIRS.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.PLASTICO_SLAB.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        buttonBlock(((ButtonBlock) ModBlocks.PLASTICO_BUTTON.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.PLASTICO_PRESSURE_PLATE.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        fenceBlock(((FenceBlock) ModBlocks.PLASTICO_FENCE.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.PLASTICO_FENCE_GATE.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.PLASTICO_WALL.get()), blockTexture(ModBlocks.PLASTICO_BLOCK.get()));

        // Para puertas y trampillas, "cutout" es el render type para texturas con transparencia (como las de hierro)
        doorBlockWithRenderType(((DoorBlock) ModBlocks.PLASTICO_DOOR.get()), modLoc("block/plastico_door_bottom"), modLoc("block/plastico_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.PLASTICO_TRAPDOOR.get()), modLoc("block/plastico_trapdoor"), true, "cutout");

        // --- SET DE ALUMINIO (Usando tu nueva estructura) ---
        stairsBlock(((StairBlock) ModBlocks.ALUMINIO_STAIRS.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.ALUMINIO_SLAB.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        buttonBlock(((ButtonBlock) ModBlocks.ALUMINIO_BUTTON.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.ALUMINIO_PRESSURE_PLATE.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        fenceBlock(((FenceBlock) ModBlocks.ALUMINIO_FENCE.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.ALUMINIO_FENCE_GATE.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.ALUMINIO_WALL.get()), blockTexture(ModBlocks.ALUMINIO_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) ModBlocks.ALUMINIO_DOOR.get()), modLoc("block/aluminio_door_bottom"), modLoc("block/aluminio_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.ALUMINIO_TRAPDOOR.get()), modLoc("block/aluminio_trapdoor"), true, "cutout");

        makeLechugaCrop((CropBlock) ModBlocks.LECHUGA_CROP.get(), "lechuga_stage", "lechuga_stage");
    }

    public void makeLechugaCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> lechugaStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] lechugaStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((LechugaCropBlock) block).getAgeProperty()),
                new ResourceLocation(SedMod.MOD_ID, "block/" + textureName + state.getValue(((LechugaCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    // Helper para bloques con item de bloque (la mayoría)
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}