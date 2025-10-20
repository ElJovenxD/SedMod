package net.eljovenxd.sedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.sleep.SleepStorage; // <-- Importante: Cambiamos al storage de sueño
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FatigueOverlay {
    private static final ResourceLocation FATIGUE_ICONS = new ResourceLocation(SedMod.MOD_ID, "textures/gui/fatigue_icons.png");

    public static final IGuiOverlay HUD_FATIGUE = (gui, guiGraphics, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        // Comprobamos que el jugador no esté en modo creativo o espectador
        if(minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.CREATIVE && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {

            // --- ESTA ES LA CORRECCIÓN CLAVE ---
            // Le pedimos la "capacidad" de sueño directamente al jugador, igual que con la sed.
            minecraft.player.getCapability(SleepStorage.SLEEP).ifPresent(sleep -> {

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, FATIGUE_ICONS);

                int fatigueLevel = sleep.getFatigue(); // Obtenemos el valor de la fatiga (0-100)
                int filledIcons = fatigueLevel / 10;   // Lo convertimos a 10 iconos

                // Posición Y, alineada con la barra de salud y hambre
                int y = height - 39;

                for (int i = 0; i < 10; i++) {
                    // Posición X, a la izquierda de la hotbar
                    int x = (width / 2) - 91 + (i * 8);

                    // Dibujamos el fondo del icono (vacío)
                    guiGraphics.blit(FATIGUE_ICONS, x, y, 9, 0, 9, 9);

                    // Si corresponde, dibujamos el icono lleno encima
                    if (i < filledIcons) {
                        guiGraphics.blit(FATIGUE_ICONS, x, y, 0, 0, 9, 9);
                    }
                }
            });
        }
    };
}