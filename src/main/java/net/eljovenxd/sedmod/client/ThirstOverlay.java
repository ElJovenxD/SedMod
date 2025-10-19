package net.eljovenxd.sedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType; // <-- IMPORTA ESTO
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ThirstOverlay {
    private static final ResourceLocation THIRST_ICONS = new ResourceLocation(SedMod.MOD_ID, "textures/gui/thirst_icons.png");

    public static final IGuiOverlay HUD_THIRST = (gui, guiGraphics, partialTick, width, height) -> {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        // --- ESTA ES LA CORRECCIÓN ---
        // Comprueba si el modo de juego del cliente NO es creativo o espectador
        if(Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.getPlayerMode() != GameType.CREATIVE && Minecraft.getInstance().gameMode.getPlayerMode() != GameType.SPECTATOR) {
            Minecraft.getInstance().player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, THIRST_ICONS);

                int thirstLevel = thirst.getThirst();
                int y = height - 50;

                for (int i = 0; i < 10; i++) {
                    int x = (width / 2) + 82 - (i * 8);

                    // Dibuja el fondo gris de la gota
                    guiGraphics.blit(THIRST_ICONS, x, y, 9, 0, 9, 9, 18, 81);

                    if (thirstLevel > i * 2) {
                        guiGraphics.blit(THIRST_ICONS, x, y, 0, 0, 9, 9, 18, 81);
                    }
                }
            });
        }
    };
}