package net.eljovenxd.sedmod.sounds;

import net.eljovenxd.sedmod.SedMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SedMod.MOD_ID);

    // 🔊 Sonido al despertar
    public static final RegistryObject<SoundEvent> WAKE_UP = registerSoundEvent("wake_up");

    // --- AÑADE ESTA LÍNEA ---
    // 🥱 Sonido de bostezo
    public static final RegistryObject<SoundEvent> BOSTEZO = registerSoundEvent("bostezo");

    // 💨 Sonido de eructo
    public static final RegistryObject<SoundEvent> ERUPTO = registerSoundEvent("erupto");
    // --- FIN DE LA LÍNEA ---

    // 🎵 (Opcional) Sonido extra que ya tenías
    public static final RegistryObject<SoundEvent> LINKIN_PARK_IN_THE_END = registerSoundEvent("linkin_park_in_the_end");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(SedMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}