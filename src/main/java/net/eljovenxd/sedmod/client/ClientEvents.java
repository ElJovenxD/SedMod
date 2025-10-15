package net.eljovenxd.sedmod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = "sedmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(new ResourceLocation("minecraft", "food_level"), "thirst", ThirstOverlay.HUD_THIRST);
    }
}
