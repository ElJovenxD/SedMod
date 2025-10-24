package net.eljovenxd.sedmod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;
// --- IMPORTA EL NUEVO OVERLAY ---
import net.eljovenxd.sedmod.client.FatigueEffectOverlay;

@Mod.EventBusSubscriber(modid = "sedmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // Registra la sed SOBRE la comida
        event.registerAbove(new ResourceLocation("minecraft", "food_level"), "thirst", ThirstOverlay.HUD_THIRST);

        // Registra la fatiga SOBRE la sed
        event.registerAbove(new ResourceLocation("sedmod", "thirst"), "fatigue", FatigueOverlay.HUD_FATIGUE);

        event.registerAbove(new ResourceLocation("minecraft", "vignette"), "fatigue_effects", FatigueEffectOverlay.HUD_FATIGUE_EFFECTS);
        // --- FIN DE LA LÍNEA ---
    }
}