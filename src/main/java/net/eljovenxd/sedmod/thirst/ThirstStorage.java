package net.eljovenxd.sedmod.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ThirstStorage {
    public static final Capability<IThirst> THIRST = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IThirst.class);
    }

    public static CompoundTag saveNBTData(IThirst thirst) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("thirst", thirst.getThirst());
        return nbt;
    }

    public static IThirst loadNBTData(CompoundTag nbt) {
        IThirst thirst = new Thirst();
        thirst.setThirst(nbt.getInt("thirst"));
        return thirst;
    }
}