package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // Lista de menas y raw de plástico
    private static final List<ItemLike> PLASTICO_ORES = List.of(ModItems.RAW_PLASTICO.get(),
            ModBlocks.PLASTICO_ORE.get(), ModBlocks.DEEPSLATE_PLASTICO_ORE.get());
    // Lista de menas y raw de aluminio
    private static final List<ItemLike> ALUMINIO_ORES = List.of(ModItems.RAW_ALUMINIO.get(),
            ModBlocks.ALUMINIO_ORE.get(), ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // --- RECETAS DE FUNDICIÓN (SMELTING & BLASTING) ---

        // Plástico (Original)
        oreSmelting(consumer, PLASTICO_ORES, RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 0.25f, 200, "plastico_ingot");
        oreBlasting(consumer, PLASTICO_ORES, RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 0.25f, 100, "plastico_ingot");

        // Aluminio (Añadido)
        oreSmelting(consumer, ALUMINIO_ORES, RecipeCategory.MISC, ModItems.ALUMINIO_INGOT.get(), 0.25f, 200, "aluminio_ingot");
        oreBlasting(consumer, ALUMINIO_ORES, RecipeCategory.MISC, ModItems.ALUMINIO_INGOT.get(), 0.25f, 100, "aluminio_ingot");


        // --- BLOQUES 9x9 ---

        // Bloque de Plástico (Original)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLASTICO_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);

        // Bloque de Aluminio (Original)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALUMINIO_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);


        // --- DE BLOQUES A LINGOTES (SHAPELESS) ---

        // Lingote de Plástico (Original)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLASTICO_INGOT.get(), 9)
                .requires(ModBlocks.PLASTICO_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.PLASTICO_BLOCK.get()), has(ModBlocks.PLASTICO_BLOCK.get()))
                .save(consumer);

        // Lingote de Aluminio (Original)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALUMINIO_INGOT.get(), 9)
                .requires(ModBlocks.ALUMINIO_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.ALUMINIO_BLOCK.get()), has(ModBlocks.ALUMINIO_BLOCK.get()))
                .save(consumer);


        // --- RECETAS DE PLÁSTICO (Añadidas) ---

        stairBuilder(ModBlocks.PLASTICO_STAIRS.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLASTICO_SLAB.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        buttonBuilder(ModBlocks.PLASTICO_BUTTON.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        pressurePlateBuilder(RecipeCategory.REDSTONE, ModBlocks.PLASTICO_PRESSURE_PLATE.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        doorBuilder(ModBlocks.PLASTICO_DOOR.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        trapdoorBuilder(ModBlocks.PLASTICO_TRAPDOOR.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));
        wallBuilder(RecipeCategory.DECORATIONS, ModBlocks.PLASTICO_WALL.get(), Ingredient.of(ModItems.PLASTICO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PLASTICO_FENCE.get(), 3)
                .pattern("ISI")
                .pattern("ISI")
                .define('I', ModItems.PLASTICO_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PLASTICO_FENCE_GATE.get())
                .pattern("SIS")
                .pattern("SIS")
                .define('I', ModItems.PLASTICO_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);


        // --- RECETAS DE ALUMINIO (Añadidas) ---

        stairBuilder(ModBlocks.ALUMINIO_STAIRS.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALUMINIO_SLAB.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        buttonBuilder(ModBlocks.ALUMINIO_BUTTON.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        pressurePlateBuilder(RecipeCategory.REDSTONE, ModBlocks.ALUMINIO_PRESSURE_PLATE.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        doorBuilder(ModBlocks.ALUMINIO_DOOR.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        trapdoorBuilder(ModBlocks.ALUMINIO_TRAPDOOR.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));
        wallBuilder(RecipeCategory.DECORATIONS, ModBlocks.ALUMINIO_WALL.get(), Ingredient.of(ModItems.ALUMINIO_INGOT.get()))
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ALUMINIO_FENCE.get(), 3)
                .pattern("ISI")
                .pattern("ISI")
                .define('I', ModItems.ALUMINIO_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.ALUMINIO_FENCE_GATE.get())
                .pattern("SIS")
                .pattern("SIS")
                .define('I', ModItems.ALUMINIO_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);


        // --- RECETAS MISCELÁNEAS (Añadidas) ---

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SOUND_BLOCK.get())
                .pattern("III")
                .pattern("INI")
                .pattern("III")
                .define('I', ModItems.ALUMINIO_INGOT.get()) // Puedes usar el lingote que prefieras
                .define('N', Blocks.NOTE_BLOCK) // Requiere un bloque de música
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        // --- RECETAS DE ARMADURA DE PLÁSTICO (AÑADIR ESTO) ---

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PLASTICO_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PLASTICO_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PLASTICO_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PLASTICO_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.PLASTICO_INGOT.get())
                .unlockedBy(getHasName(ModItems.PLASTICO_INGOT.get()), has(ModItems.PLASTICO_INGOT.get()))
                .save(consumer);


        // --- RECETAS DE ARMADURA DE ALUMINIO (AÑADIR ESTO) ---

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ALUMINIO_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ALUMINIO_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ALUMINIO_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ALUMINIO_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.ALUMINIO_INGOT.get())
                .unlockedBy(getHasName(ModItems.ALUMINIO_INGOT.get()), has(ModItems.ALUMINIO_INGOT.get()))
                .save(consumer);

        // --- RECETA DE SEMILLAS DE LECHUGA (Añadido) ---
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LECHUGA_SEEDS.get(), 1)
                .requires(ModItems.LECHUGA.get())
                .unlockedBy(getHasName(ModItems.LECHUGA.get()), has(ModItems.LECHUGA.get()))
                .save(consumer);
    }


    // --- MÉTODOS HELPER (Originales) ---

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
                    .save(pFinishedRecipeConsumer,  getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}