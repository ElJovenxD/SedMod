package net.eljovenxd.sedmod.thirst;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThirstProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final IThirst thirst = new Thirst();
    private final LazyOptional<IThirst> optional = LazyOptional.of(() -> thirst);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ThirstStorage.THIRST ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return ThirstStorage.saveNBTData(thirst);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ThirstStorage.loadNBTData(nbt);
    }
}
