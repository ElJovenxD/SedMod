package net.eljovenxd.sedmod.datagen;


import net.eljovenxd.sedmod.SedMod;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SedMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // --- SOLO SE AÑADE UNA VEZ AQUÍ ---
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput)); // Para recetas

        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput)); // Para tablas de botín (loot tables)

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper)); // Para estados y modelos de bloques
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper)); // Para modelos de ítems

        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper)); // Para etiquetas de bloques (tags)

        generator.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper)); // Para etiquetas de ítems (tags)

        generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(packOutput));
        generator.addProvider(event.includeServer(), new ModPoiTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
    }
}