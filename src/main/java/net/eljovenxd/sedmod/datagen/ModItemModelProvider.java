package net.eljovenxd.sedmod.datagen;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Items simples
        simpleItem(ModItems.RAW_PLASTICO);
        simpleItem(ModItems.PLASTICO_INGOT);
        simpleItem(ModItems.MARUCHAN);
        simpleItem(ModItems.COCA);
        simpleItem(ModItems.PEPSI);
        simpleItem(ModItems.AGUA);
        simpleItem(ModItems.LATA_COMBUSTIBLE);

        // --- Items de Aluminio ---
        simpleItem(ModItems.RAW_ALUMINIO);
        simpleItem(ModItems.ALUMINIO_INGOT);

        // Items con modelo custom (Handheld)
        handheldItem(ModItems.METAL_DETECTOR);
    }

    private void simpleItem(RegistryObject<?> item) {
        withExistingParent(item.getId().getPath(),
                "item/generated").texture("layer0",
                modLoc("item/" + item.getId().getPath()));
    }

    private void handheldItem(RegistryObject<?> item) {
        withExistingParent(item.getId().getPath(),
                "item/handheld").texture("layer0",
                modLoc("item/" + item.getId().getPath())); // <-- Sin la 'J'
    }
}