package net.eljovenxd.sedmod.client;

import net.eljovenxd.sedmod.SedMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// --- AÑADE "bus = Mod.EventBusSubscriber.Bus.MOD" AQUÍ ---
@Mod.EventBusSubscriber(modid = SedMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // La barra de sed se queda como está, a la derecha.
        event.registerAbove(VanillaGuiOverlay.ARMOR_LEVEL.id(), "thirst", ThirstOverlay.HUD_THIRST);

        // Registramos la barra de fatiga POR ENCIMA de la barra de salud (corazones).
        // Esto la posiciona correctamente en el lado izquierdo de la pantalla.
        event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "fatigue", FatigueOverlay.HUD_FATIGUE);
    }
}