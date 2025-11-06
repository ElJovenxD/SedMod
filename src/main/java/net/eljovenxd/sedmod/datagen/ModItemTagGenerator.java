package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, SedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.PLASTICO_HELMET.get(),
                        ModItems.PLASTICO_CHESTPLATE.get(),
                        ModItems.PLASTICO_LEGGINGS.get(),
                        ModItems.PLASTICO_BOOTS.get());

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ALUMINIO_HELMET.get(),
                        ModItems.ALUMINIO_CHESTPLATE.get(),
                        ModItems.ALUMINIO_LEGGINGS.get(),
                        ModItems.ALUMINIO_BOOTS.get());

        this.tag(ItemTags.MUSIC_DISCS)
                .add(ModItems.RISE_MUSIC_DISC.get())
                .add(ModItems.WARRIORS_MUSIC_DISC.get())
                .add(ModItems.LIGHT_AND_SHADOW_MUSIC_DISC.get());

        this.tag(ItemTags.CREEPER_DROP_MUSIC_DISCS)
                .add(ModItems.RISE_MUSIC_DISC.get());
    }
}
