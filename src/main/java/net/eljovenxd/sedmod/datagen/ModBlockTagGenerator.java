package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Añade TODOS tus minerales a la etiqueta, como en tu JSON
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.ALUMINIO_ORE.get())
                .add(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get())
                .add(ModBlocks.PLASTICO_ORE.get())
                .add(ModBlocks.DEEPSLATE_PLASTICO_ORE.get())
                .addTag(Tags.Blocks.ORES); // Esto añade "#forge:ores"
    }
}