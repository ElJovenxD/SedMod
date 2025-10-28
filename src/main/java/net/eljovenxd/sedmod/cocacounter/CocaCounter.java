package net.eljovenxd.sedmod.cocacounter;

import net.minecraft.nbt.CompoundTag;

public class CocaCounter implements ICocaCounter {
    private int cocaCounter = 0;
    private final int MIN_COUNTER = 0;
    private final int MAX_COUNTER = 10;

    @Override
    public int getCocaCounter() {
        return cocaCounter;
    }

    @Override
    public void setCocaCounter(int count) {
        this.cocaCounter = Math.max(MIN_COUNTER, Math.min(count, MAX_COUNTER));
    }

    @Override
    public void addCocaCounter(int count) {
        setCocaCounter(this.cocaCounter + count);
    }

    @Override
    public void subCocaCounter(int count) {
        setCocaCounter(this.cocaCounter - count);
    }

    // --- Nuevo manejo de NBT ---
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("cocaCounter", cocaCounter);
    }

    public void loadNBTData(CompoundTag nbt) {
        cocaCounter = nbt.getInt("cocaCounter");
    }
}
