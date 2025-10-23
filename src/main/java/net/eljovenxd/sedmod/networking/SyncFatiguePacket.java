package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncFatiguePacket {
    private final int fatigue;

    public SyncFatiguePacket(int fatigue) {
        this.fatigue = fatigue;
    }

    public SyncFatiguePacket(FriendlyByteBuf buf) {
        this.fatigue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fatigue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Lado del cliente
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    fatigue.setFatigue(this.fatigue);
                });
            }
        });
        return true;
    }
}