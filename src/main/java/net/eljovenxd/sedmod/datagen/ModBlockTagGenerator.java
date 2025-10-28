package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // Tag para el Metal Detector (Este ya lo tenías)
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.ALUMINIO_ORE.get(),
                        ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());

        this.tag(ModTags.Blocks.NEEDS_ALUMINIO_TOOLS)
                .add(ModBlocks.ALUMINIO_ORE.get());

        this.tag(ModTags.Blocks.NEEDS_PLASTICO_TOOLS)
                .add(ModBlocks.PLASTICO_ORE.get());

        // TAG PARA HERRAMIENTA (PICO)
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CAJA_COCA.get(),
                        ModBlocks.CAJA_PEPSI.get(),
                        ModBlocks.PLASTICO_ORE.get(),
                        ModBlocks.DEEPSLATE_PLASTICO_ORE.get(),
                        ModBlocks.ALUMINIO_ORE.get(),
                        ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(),
                        ModBlocks.PLASTICO_BLOCK.get(),
                        ModBlocks.ALUMINIO_BLOCK.get(),
                        ModBlocks.PLASTICO_STAIRS.get(),
                        ModBlocks.PLASTICO_SLAB.get(),
                        ModBlocks.PLASTICO_BUTTON.get(),
                        ModBlocks.PLASTICO_PRESSURE_PLATE.get(),
                        ModBlocks.PLASTICO_FENCE.get(),
                        ModBlocks.PLASTICO_FENCE_GATE.get(),
                        ModBlocks.PLASTICO_WALL.get(),
                        ModBlocks.PLASTICO_DOOR.get(),
                        ModBlocks.PLASTICO_TRAPDOOR.get(),
                        ModBlocks.ALUMINIO_STAIRS.get(),
                        ModBlocks.ALUMINIO_SLAB.get(),
                        ModBlocks.ALUMINIO_BUTTON.get(),
                        ModBlocks.ALUMINIO_PRESSURE_PLATE.get(),
                        ModBlocks.ALUMINIO_FENCE.get(),
                        ModBlocks.ALUMINIO_FENCE_GATE.get(),
                        ModBlocks.ALUMINIO_WALL.get(),
                        ModBlocks.ALUMINIO_DOOR.get(),
                        ModBlocks.ALUMINIO_TRAPDOOR.get(),
                        ModBlocks.SOUND_BLOCK.get());

        // TAG PARA NIVEL DE HERRAMIENTA (PICO DE PIEDRA)
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.PLASTICO_ORE.get(),
                        ModBlocks.DEEPSLATE_PLASTICO_ORE.get(),
                        ModBlocks.ALUMINIO_ORE.get(),
                        ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(),
                        ModBlocks.PLASTICO_BLOCK.get(),
                        ModBlocks.ALUMINIO_BLOCK.get(),
                        ModBlocks.PLASTICO_STAIRS.get(),
                        ModBlocks.PLASTICO_SLAB.get(),
                        ModBlocks.PLASTICO_WALL.get(),
                        ModBlocks.ALUMINIO_STAIRS.get(),
                        ModBlocks.ALUMINIO_SLAB.get(),
                        ModBlocks.ALUMINIO_WALL.get(),
                        ModBlocks.SOUND_BLOCK.get(),
                        ModBlocks.CAJA_COCA.get(),
                        ModBlocks.CAJA_PEPSI.get());

        // --- FIN DE LO AÑADIDO ---
    }
}