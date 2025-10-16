package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.client.Minecraft; // <- IMPORTANTE: Añade esta línea
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncThirstPacket {
    private final int thirst;

    public SyncThirstPacket(int thirst) {
        this.thirst = thirst;
    }

    public SyncThirstPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // --- ESTA ES LA CORRECCIÓN ---
            // En el lado del cliente, debemos obtener el jugador local usando Minecraft.getInstance().player
            // ya que 'context.getSender()' es nulo aquí.
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.setThirst(this.thirst);
                });
            }
        });
        return true;
    }
}