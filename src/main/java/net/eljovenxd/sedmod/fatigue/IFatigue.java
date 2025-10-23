package net.eljovenxd.sedmod.fatigue;

public interface IFatigue {
    int getFatigue();
    void setFatigue(int fatigue);
    void addFatigue(int fatigue);
    void removeFatigue(int fatigue);

    // --- AÑADE ESTOS MÉTODOS ---
    long getLastSleepTime();
    void setLastSleepTime(long time);
    boolean isSleeping();
    void setSleeping(boolean sleeping);
    // --- FIN ---
}