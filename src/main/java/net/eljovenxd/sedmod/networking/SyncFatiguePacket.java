package net.eljovenxd.sedmod.networking;

import net.eljovenxd.sedmod.sleep.SleepStorage;
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
        buf.writeInt(this.fatigue);
    }

    // --- ESTE ES EL CÓDIGO CORRECTO Y DEFINITIVO ---
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Se ejecuta en el cliente.
            // Le entregamos el dato de fatiga DIRECTAMENTE al jugador.
            if(Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(SleepStorage.SLEEP).ifPresent(sleep -> {
                    sleep.setFatigue(this.fatigue);
                });
            }
        });
        return true;
    }
}