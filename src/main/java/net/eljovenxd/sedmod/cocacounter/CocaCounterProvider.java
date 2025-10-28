package net.eljovenxd.sedmod.cocacounter;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CocaCounterProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ICocaCounter> COCA_COUNTER_CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final CocaCounter cocaCounter = new CocaCounter();
    private final LazyOptional<ICocaCounter> optional = LazyOptional.of(() -> cocaCounter);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == COCA_COUNTER_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        cocaCounter.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        cocaCounter.loadNBTData(nbt);
    }
}
