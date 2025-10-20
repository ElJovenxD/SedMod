package net.eljovenxd.sedmod.sleep;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SleepStorage {
    public static final Capability<ISleep> SLEEP = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(ISleep.class);
    }

    public static CompoundTag saveNBTData(ISleep sleep) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("fatigue", sleep.getFatigue());
        return nbt;
    }

    public static ISleep loadNBTData(CompoundTag nbt) {
        ISleep sleep = new Sleep();
        sleep.setFatigue(nbt.getInt("fatigue"));
        return sleep;
    }
}
