package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncThirstPacket {
    private final int thirst;
    private final float saturation; // <-- AÑADE ESTO

    // Actualiza el constructor
    public SyncThirstPacket(int thirst, float saturation) {
        this.thirst = thirst;
        this.saturation = saturation; // <-- AÑADE ESTO
    }

    // Actualiza el constructor del buffer
    public SyncThirstPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
        this.saturation = buf.readFloat(); // <-- AÑADE ESTO
    }

    // Actualiza toBytes
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
        buf.writeFloat(saturation); // <-- AÑADE ESTO
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // En el lado del cliente
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.setThirst(this.thirst);
                    thirst.setThirstSaturation(this.saturation); // <-- AÑADE ESTO
                });
            }
        });
        return true;
    }
}