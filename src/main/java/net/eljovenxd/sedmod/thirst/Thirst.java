package net.eljovenxd.sedmod.thirst;

public class Thirst implements IThirst {
    private int thirst = 20; // Nivel de sed máximo

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
}
