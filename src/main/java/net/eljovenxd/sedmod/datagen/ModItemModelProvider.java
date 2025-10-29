package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem; // Se necesita esta importación
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile; // Se necesita esta importación
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.server.packs.PackType; // Se necesita esta importación

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

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

        trimmedArmorItem(ModItems.PLASTICO_HELMET);
        trimmedArmorItem(ModItems.PLASTICO_CHESTPLATE);
        trimmedArmorItem(ModItems.PLASTICO_LEGGINGS);
        trimmedArmorItem(ModItems.PLASTICO_BOOTS);

        trimmedArmorItem(ModItems.ALUMINIO_HELMET);
        trimmedArmorItem(ModItems.ALUMINIO_CHESTPLATE);
        trimmedArmorItem(ModItems.ALUMINIO_LEGGINGS);
        trimmedArmorItem(ModItems.ALUMINIO_BOOTS);
    }


    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = SedMod.MOD_ID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                // --- LÍNEA CORREGIDA ---
                // Se cambió 'armorItem' (el objeto) por 'itemRegistryObject.getId().getPath()' (el nombre de registro)
                String armorItemPath = "item/" + itemRegistryObject.getId().getPath();
                // --- FIN DE LA CORRECCIÓN ---

                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

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