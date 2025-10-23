package net.eljovenxd.sedmod.networking;

// --- AÑADE ESTE IMPORT ---
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
// --- FIN DEL IMPORT ---

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
                .named(new ResourceLocation("sedmod", "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // Paquete de Sed (Existente)
        net.messageBuilder(SyncThirstPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncThirstPacket::new)
                .encoder(SyncThirstPacket::toBytes)
                .consumerMainThread(SyncThirstPacket::handle)
                .add();

        // --- ESTE ES EL BLOQUE CORREGIDO Y LIMPIO ---
        // Paquete de Fatiga (Nuevo)
        // (Asegúrate de haber movido tu archivo SyncFatiguePacket.java a esta carpeta "networking")
        net.messageBuilder(SyncFatiguePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncFatiguePacket::new)
                .encoder(SyncFatiguePacket::toBytes)
                .consumerMainThread(SyncFatiguePacket::handle)
                .add();
        // --- FIN DEL BLOQUE CORREGIDO ---
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}