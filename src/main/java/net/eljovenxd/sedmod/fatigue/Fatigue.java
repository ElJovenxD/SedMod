package net.eljovenxd.sedmod.fatigue;

public class Fatigue implements IFatigue {
    private int fatigue = 20; // Valor inicial (máximo)

    @Override
    public int getFatigue() {
        return this.fatigue;
    }

    @Override
    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(fatigue, 20));
    }

    @Override
    public void addFatigue(int fatigue) {
        this.setFatigue(this.fatigue + fatigue);
    }

    @Override
    public void removeFatigue(int fatigue) {
        this.setFatigue(this.fatigue - fatigue);
    }
}