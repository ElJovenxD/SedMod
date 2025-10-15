package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.thirst.ThirstStorage;
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
            // Del lado del cliente
            context.getSender().getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                thirst.setThirst(this.thirst);
            });
        });
        return true;
    }
}
