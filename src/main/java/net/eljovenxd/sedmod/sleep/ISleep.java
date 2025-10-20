package net.eljovenxd.sedmod.sleep;

public interface ISleep {
    int getFatigue();           // 0 - 100
    void setFatigue(int fatigue);
    void addFatigue(int amount);
    void removeFatigue(int amount);
}
