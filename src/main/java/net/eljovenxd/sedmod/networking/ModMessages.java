package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.SedMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SedMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // --- Paquete de Sed (ya estaba bien) ---
        net.messageBuilder(SyncThirstPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncThirstPacket::new)
                .encoder(SyncThirstPacket::toBytes)
                .consumerMainThread(SyncThirstPacket::handle)
                .add();

        // --- Paquete de Fatiga (AQUÍ ESTÁ LA CORRECCIÓN) ---
        net.messageBuilder(SyncFatiguePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncFatiguePacket::new)
                .encoder(SyncFatiguePacket::toBytes)
                .consumerMainThread(SyncFatiguePacket::handle) // <<-- ESTA LÍNEA FALTABA
                .add();
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}