package net.eljovenxd.sedmod;

import com.mojang.logging.LogUtils;
import net.eljovenxd.sedmod.ModEvents.ModEvents;
import net.eljovenxd.sedmod.block.ModBlocks;
// --- AÑADE ESTE IMPORT ---
import net.eljovenxd.sedmod.ModEvents.ModCommands;
// --- FIN DEL IMPORT ---
import net.eljovenxd.sedmod.datagen.DataGenerators;
import net.eljovenxd.sedmod.item.ModCreativeModTabs;
import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.sounds.ModSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.eljovenxd.sedmod.networking.ModMessages;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SedMod.MOD_ID)
public class SedMod {
    public static final String MOD_ID = "sedmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SedMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new ModEvents());

        modEventBus.addListener(this::addCreative);

        ModMessages.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.COCA);
            event.accept(ModItems.PEPSI);
            event.accept(ModItems.AGUA);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // --- AÑADE ESTA LÍNEA ---
        // Esto registra tu clase de comandos con el servidor de Minecraft
        ModCommands.register(event.getServer().getCommands().getDispatcher());
        // --- FIN DE LA CORRECCIÓN ---
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}