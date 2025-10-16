package net.eljovenxd.sedmod.thirst;

public class Thirst implements IThirst {
    private int thirst = 20;
    private float thirstSaturation = 5.0F; // <-- Valor inicial de saturación

    @Override
    public int getThirst() {
        return thirst;
    }

    @Override
    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    @Override
    public void addThirst(int thirst) {
        this.thirst = Math.min(this.thirst + thirst, 20);
    }

    @Override
    public void removeThirst(int thirst) {
        this.thirst = Math.max(this.thirst - thirst, 0);
    }

    // --- AÑADE ESTOS MÉTODOS ---
    @Override
    public float getThirstSaturation() {
        return this.thirstSaturation;
    }

    @Override
    public void setThirstSaturation(float saturation) {
        this.thirstSaturation = saturation;
    }

    @Override
    public void addThirstSaturation(float saturation) {
        // La saturación no puede exceder el nivel de sed actual.
        this.thirstSaturation = Math.min(this.thirstSaturation + saturation, this.thirst);
    }
}
