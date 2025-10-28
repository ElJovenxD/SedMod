package net.eljovenxd.sedmod.util;

import net.eljovenxd.sedmod.SedMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        public static final TagKey<Block> NEEDS_ALUMINIO_TOOLS = tag("needs_aluminio_tools");
        public static final TagKey<Block> NEEDS_PLASTICO_TOOLS = tag("needs_plastico_tools");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(SedMod.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(SedMod.MOD_ID, name));
        }
    }
}
