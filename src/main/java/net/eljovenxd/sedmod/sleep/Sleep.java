package net.eljovenxd.sedmod.sleep;

public class Sleep implements ISleep {
    private int fatigue = 100; // 100 = completamente descansado

    @Override
    public int getFatigue() {
        return fatigue;
    }

    @Override
    public void setFatigue(int fatigue) {
        // Límite entre 0 y 100
        this.fatigue = Math.max(0, Math.min(fatigue, 100));
    }

    @Override
    public void addFatigue(int amount) {
        // Recuperar energía (dormir, por ejemplo)
        this.fatigue = Math.min(this.fatigue + amount, 100);
    }

    @Override
    public void removeFatigue(int amount) {
        // Perder energía con el tiempo o actividad
        this.fatigue = Math.max(this.fatigue - amount, 0);
    }
}
