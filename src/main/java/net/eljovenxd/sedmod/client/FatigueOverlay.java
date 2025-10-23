package net.eljovenxd.sedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.fatigue.FatigueStorage; // <-- CAMBIO
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FatigueOverlay {
    // --- IMPORTANTE ---
    // Debes crear este nuevo archivo de textura.
    // Puedes copiar "thirst_icons.png" y cambiarle el color (ej. a amarillo o azul).
    private static final ResourceLocation FATIGUE_ICONS = new ResourceLocation(SedMod.MOD_ID, "textures/gui/fatigue_icons.png");

    public static final IGuiOverlay HUD_FATIGUE = (gui, guiGraphics, partialTick, width, height) -> {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        if(Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.getPlayerMode() != GameType.CREATIVE && Minecraft.getInstance().gameMode.getPlayerMode() != GameType.SPECTATOR) {

            // Usa la capability de FATIGA
            Minecraft.getInstance().player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, FATIGUE_ICONS); // <-- Usa la nueva textura

                int fatigueLevel = fatigue.getFatigue();

                // --- POSICIÓN Y ---
                // La sed está en (height - 50). La ponemos 9 píxeles más arriba.
                int y = height - 59;

                for (int i = 0; i < 10; i++) {
                    int x = (width / 2) + 82 - (i * 8);

                    // Dibuja el fondo gris
                    guiGraphics.blit(FATIGUE_ICONS, x, y, 9, 0, 9, 9, 18, 81);

                    // Dibuja el icono de fatiga (debe ser la misma disposición que thirst_icons)
                    if (fatigueLevel > i * 2) {
                        // Asumiendo que el icono lleno está en (0,0)
                        guiGraphics.blit(FATIGUE_ICONS, x, y, 0, 0, 9, 9, 18, 81);
                    }
                }
            });
        }
    };
}