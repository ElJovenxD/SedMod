package net.eljovenxd.sedmod.fatigue;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class FatigueStorage {

    // Define el token de la capability
    public static final Capability<IFatigue> FATIGUE = CapabilityManager.get(new CapabilityToken<IFatigue>() { });

    // Método para registrar la capability
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IFatigue.class);
    }
}