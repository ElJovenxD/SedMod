package net.eljovenxd.sedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ThirstOverlay {
    // La ruta a tu imagen (la que hiciste de 18x81 pixeles)
    private static final ResourceLocation THIRST_BAR = new ResourceLocation(SedMod.MOD_ID, "textures/gui/thirst_icons.png");

    public static final IGuiOverlay HUD_THIRST = (gui, guiGraphics, partialTick, width, height) -> {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        Minecraft.getInstance().player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
            // Preparar el sistema de renderizado
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, THIRST_BAR);

            // Posición de la barra en la pantalla (a la derecha de la barra de experiencia)
            int x = width / 2 + 92;
            int y = height - 49; // Alineada con la parte de arriba de la barra de experiencia

            // --- 1. Dibuja la barra de fondo (la versión gris) ---
            // La dibujamos completa, con sus 81 píxeles de alto.
            // El '9' en U significa que empieza a leer desde el pixel 9 de la imagen (la segunda mitad)
            guiGraphics.blit(THIRST_BAR, x, y, 9, 0, 9, 81);

            // --- 2. Dibuja la barra de color encima ---
            int thirstLevel = thirst.getThirst();
            // Calcula la ALTURA de la barra a dibujar (de 0 a 81 pixeles)
            int barHeight = (int)(((float)thirstLevel / 20.0F) * 81.0F);

            // Esta es la parte importante: dibujamos la barra desde abajo hacia arriba.
            // Para ello, ajustamos la posición 'y' de la textura y de la pantalla
            // para que solo se dibuje la porción correspondiente al nivel de sed.
            guiGraphics.blit(THIRST_BAR,
                    x, y + (81 - barHeight), // Posición en pantalla (empieza más abajo si la barra no está llena)
                    0, 81 - barHeight,   // Coordenada U,V en la textura (empieza a leer más abajo en la imagen)
                    9, barHeight);        // Ancho y alto del trozo a dibujar
        });
    };
}