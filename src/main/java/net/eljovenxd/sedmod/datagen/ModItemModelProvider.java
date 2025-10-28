package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // --- Items Simples ---
        simpleItem(ModItems.COCA);
        simpleItem(ModItems.PEPSI);
        simpleItem(ModItems.AGUA);
        simpleItem(ModItems.LATA_COMBUSTIBLE);
        simpleItem(ModItems.MARUCHAN);
        simpleItem(ModItems.RAW_PLASTICO);
        simpleItem(ModItems.RAW_ALUMINIO);
        simpleItem(ModItems.PLASTICO_INGOT);
        simpleItem(ModItems.ALUMINIO_INGOT);
        simpleItem(ModItems.METAL_DETECTOR);

        // --- AÑADE ESTAS LÍNEAS ---
        simpleItem(ModItems.PIEDRITA); // 'simpleItem' porque es un item normal
        handheldItem(ModItems.PLASTICO_CUCHILLO); // 'handheldItem' porque es una herramienta/arma
        handheldItem(ModItems.ALUMINIO_CUCHILLO); // 'handheldItem' porque es una herramienta/arma
        // --- FIN ---

        // --- Herramientas de Aluminio (3D) ---
        handheldItem(ModItems.ALUMINIO_SWORD);
        handheldItem(ModItems.ALUMINIO_PICKAXE);
        handheldItem(ModItems.ALUMINIO_AXE);
        handheldItem(ModItems.ALUMINIO_SHOVEL);
        handheldItem(ModItems.ALUMINIO_HOE);

        // --- Herramientas de Plástico (3D) ---
        handheldItem(ModItems.PLASTICO_SWORD);
        handheldItem(ModItems.PLASTICO_PICKAXE);
        handheldItem(ModItems.PLASTICO_AXE);
        handheldItem(ModItems.PLASTICO_SHOVEL);
        handheldItem(ModItems.PLASTICO_HOE);

        // --- Set de Plástico ---
        simpleBlockItem(ModBlocks.PLASTICO_DOOR); // Item de puerta (usa textura .png)
        fenceItem(ModBlocks.PLASTICO_FENCE, ModBlocks.PLASTICO_BLOCK);
        buttonItem(ModBlocks.PLASTICO_BUTTON, ModBlocks.PLASTICO_BLOCK);
        wallItem(ModBlocks.PLASTICO_WALL, ModBlocks.PLASTICO_BLOCK);

        evenSimplerBlockItem(ModBlocks.PLASTICO_STAIRS); // Modelo de bloque existente
        evenSimplerBlockItem(ModBlocks.PLASTICO_SLAB);
        evenSimplerBlockItem(ModBlocks.PLASTICO_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.PLASTICO_FENCE_GATE);

        trapdoorItem(ModBlocks.PLASTICO_TRAPDOOR); // Modelo especial _bottom

        // --- Set de Aluminio ---
        simpleBlockItem(ModBlocks.ALUMINIO_DOOR); // Item de puerta
        fenceItem(ModBlocks.ALUMINIO_FENCE, ModBlocks.ALUMINIO_BLOCK);
        buttonItem(ModBlocks.ALUMINIO_BUTTON, ModBlocks.ALUMINIO_BLOCK);
        wallItem(ModBlocks.ALUMINIO_WALL, ModBlocks.ALUMINIO_BLOCK);

        evenSimplerBlockItem(ModBlocks.ALUMINIO_STAIRS);
        evenSimplerBlockItem(ModBlocks.ALUMINIO_SLAB);
        evenSimplerBlockItem(ModBlocks.ALUMINIO_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.ALUMINIO_FENCE_GATE);

        trapdoorItem(ModBlocks.ALUMINIO_TRAPDOOR);
    }


    // --- Métodos Helper (Copiados de tu ejemplo y adaptados) ---

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SedMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SedMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(SedMod.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(SedMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(SedMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(SedMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SedMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}