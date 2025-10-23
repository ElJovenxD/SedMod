package net.eljovenxd.sedmod.fatigue;

public class Fatigue implements IFatigue {
    private int fatigue = 20; // Valor inicial (máximo)

    // --- AÑADE ESTOS CAMPOS ---
    private long lastSleepTime = 0;
    private boolean isSleeping = false;
    // --- FIN ---

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

    // --- AÑADE ESTOS MÉTODOS ---
    @Override
    public long getLastSleepTime() {
        return this.lastSleepTime;
    }

    @Override
    public void setLastSleepTime(long time) {
        this.lastSleepTime = time;
    }

    @Override
    public boolean isSleeping() {
        return this.isSleeping;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.isSleeping = sleeping;
    }
    // --- FIN ---
}