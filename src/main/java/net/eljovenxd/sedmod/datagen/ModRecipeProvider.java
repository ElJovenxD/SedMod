package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> PLASTICO_SMELTABLES = List.of(ModItems.RAW_PLASTICO.get(),
            ModBlocks.PLASTICO_ORE.get(), ModBlocks.DEEPSLATE_PLASTICO_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, PLASTICO_SMELTABLES, RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 0.25F,100,"plastico_ingot");
        oreBlasting(pWriter, PLASTICO_SMELTABLES, RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 0.25F,100,"plastico_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CAJA_COCA.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', ModItems.COCA.get())
                .unlockedBy(getHasName(ModItems.COCA.get()), has(ModItems.COCA.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COCA.get(), 9)
                .requires(ModBlocks.CAJA_COCA.get())
                .unlockedBy(getHasName(ModBlocks.CAJA_COCA.get()), has(ModBlocks.CAJA_COCA.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALUMINIO_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(pWriter);

        // Aluminio (1 bloque -> 9 lingotes)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALUMINIO_INGOT.get(), 9)
                .requires(ModBlocks.ALUMINIO_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.ALUMINIO_BLOCK.get()), has(ModBlocks.ALUMINIO_BLOCK.get()))
                .save(pWriter);

        // Bloque de Plástico (9 lingotes -> 1 bloque)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLASTICO_BLOCK.get())
                .pattern("PPP")
                .pattern("PPP")
                .pattern("PPP")
                .define('P', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(pWriter);

        // Plástico (1 bloque -> 9 lingotes)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 9)
                .requires(ModBlocks.PLASTICO_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.PLASTICO_BLOCK.get()), has(ModBlocks.PLASTICO_BLOCK.get()))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, SedMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
