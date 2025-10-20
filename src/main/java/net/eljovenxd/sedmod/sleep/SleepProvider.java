package net.eljovenxd.sedmod.sleep;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SleepProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ISleep sleep = new Sleep();
    private final LazyOptional<ISleep> optional = LazyOptional.of(() -> sleep);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SleepStorage.SLEEP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("fatigue", sleep.getFatigue());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        sleep.setFatigue(nbt.getInt("fatigue"));
    }
}
