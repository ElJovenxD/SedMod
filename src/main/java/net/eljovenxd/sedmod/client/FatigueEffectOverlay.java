package net.eljovenxd.sedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FatigueEffectOverlay {

    // Variables para el apagón (sin cambios)
    private static boolean isBlackingOut = false;
    private static long blackoutStartTime = 0;
    private static final int BLACKOUT_DURATION_TICKS = 10; // Medio segundo
    private static final RandomSource random = RandomSource.create();

    public static final IGuiOverlay HUD_FATIGUE_EFFECTS = (gui, guiGraphics, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        minecraft.player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
            int fatigueLevel = fatigue.getFatigue();
            long currentGameTime = minecraft.level != null ? minecraft.level.getGameTime() : 0;

            // --- 1. Efecto PÁRPADOS con Rectángulos --- (MODIFICADO)
            if (fatigueLevel <= 10 && !isBlackingOut) { // Se activa por debajo del 50%
                float progress = Mth.clamp((10.0f - fatigueLevel) / 10.0f, 0.0f, 1.0f);

                if (progress > 0) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int color = 0xFF000000; // Negro Opaco

                    // Calculamos cuánto deben cubrir los rectángulos negros
                    float maxCoverageFactor = 0.95f; // Mantiene un hueco mínimo
                    // --- LÍNEA ELIMINADA --- (ya no necesitamos 'horizontalCoverage')
                    int verticalCoverage = (int) (height / 2.0f * progress * maxCoverageFactor);

                    // Dibuja los 2 rectángulos negros (párpados)
                    guiGraphics.fill(0, 0, width, verticalCoverage, color); // Párpado superior
                    guiGraphics.fill(0, height - verticalCoverage, width, height, color); // Párpado inferior
                    // --- LÍNEA ELIMINADA --- (ya no dibujamos la barra izquierda)
                    // --- LÍNEA ELIMINADA --- (ya no dibujamos la barra derecha)

                    RenderSystem.disableBlend();
                }
            }

            // --- 2. Efecto Apagón (Blackout) --- (SIN CAMBIOS)
            if (isBlackingOut) {
                // Si ya está ocurriendo un apagón, dibuja negro y comprueba si termina
                RenderSystem.disableBlend();
                guiGraphics.fill(0, 0, width, height, 0xFF000000);
                if (currentGameTime >= blackoutStartTime + BLACKOUT_DURATION_TICKS) {
                    isBlackingOut = false;
                }
            } else if (fatigueLevel <= 6) {
                // Si no hay apagón y la fatiga es 6 o menos, calcula la probabilidad

                // --- FRECUENCIA AJUSTADA ---
                int maxRoll = 400 - (int) ((6 - fatigueLevel) * (350.0 / 6.0));
                maxRoll = Math.max(50, maxRoll);

                if (random.nextInt(maxRoll) == 0) {
                    isBlackingOut = true; // Inicia apagón
                    blackoutStartTime = currentGameTime;
                    RenderSystem.disableBlend();
                    guiGraphics.fill(0, 0, width, height, 0xFF000000); // Dibuja inmediatamente
                }
                // --- FIN FRECUENCIA AJUSTADA ---
            }
        });
    };
}