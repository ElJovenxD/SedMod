package net.eljovenxd.sedmod.fatigue;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FatigueProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final IFatigue fatigue = new Fatigue();
    private final LazyOptional<IFatigue> optional = LazyOptional.of(() -> fatigue);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == FatigueStorage.FATIGUE ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("fatigue", fatigue.getFatigue());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        fatigue.setFatigue(nbt.getInt("fatigue"));
    }
}