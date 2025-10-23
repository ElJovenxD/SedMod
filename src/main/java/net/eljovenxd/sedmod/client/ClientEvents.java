package net.eljovenxd.sedmod.client;

// --- AÑADE ESTE IMPORT ---
import net.eljovenxd.sedmod.client.FatigueOverlay;
// --- FIN DEL IMPORT ---

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = "sedmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // Registra la sed SOBRE la comida (como antes)
        event.registerAbove(new ResourceLocation("minecraft", "food_level"), "thirst", ThirstOverlay.HUD_THIRST);

        // --- LÍNEA CORREGIDA ---
        // Registra la fatiga SOBRE la sed (para que aparezca arriba)
        event.registerAbove(new ResourceLocation("sedmod", "thirst"), "fatigue", FatigueOverlay.HUD_FATIGUE);
    }
}