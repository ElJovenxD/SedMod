package net.eljovenxd.sedmod.item;


import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier ALUMINIO = TierSortingRegistry.registerTier(
            new ForgeTier(2, 250, 6.0f, 2.0f, 14,
                    ModTags.Blocks.NEEDS_ALUMINIO_TOOLS, () -> Ingredient.of(ModItems.ALUMINIO_INGOT.get())),
            new ResourceLocation(SedMod.MOD_ID, "aluminio"),
            List.of(Tiers.STONE), List.of(Tiers.DIAMOND));

public static final Tier PLASTICO = TierSortingRegistry.registerTier(
        new ForgeTier(1, 131, 4.0f, 1.0f, 5,
                ModTags.Blocks.NEEDS_PLASTICO_TOOLS,
                () -> Ingredient.of(ModItems.PLASTICO_INGOT.get())),
        new ResourceLocation(SedMod.MOD_ID, "plastico"),
        List.of(Tiers.WOOD), List.of(Tiers.IRON));
}