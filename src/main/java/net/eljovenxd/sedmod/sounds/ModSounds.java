package net.eljovenxd.sedmod.sounds;

import net.eljovenxd.sedmod.SedMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SedMod.MOD_ID);

    // 🔊 Sonido al despertar
    public static final RegistryObject<SoundEvent> WAKE_UP = registerSoundEvent("wake_up");

    // 🥱 Sonido de bostezo
    public static final RegistryObject<SoundEvent> BOSTEZO = registerSoundEvent("bostezo");

    // 💨 Sonido de eructo
    public static final RegistryObject<SoundEvent> ERUPTO = registerSoundEvent("erupto");
    // --- FIN DE LA LÍNEA ---

    public static final RegistryObject<SoundEvent> METAL_DETECTOR_FOUND_ORE = registerSoundEvent("metal_detector_found_ore");

    public static final RegistryObject<SoundEvent> SOUND_BLOCK_BREAK = registerSoundEvent("sound_block_break");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_STEP = registerSoundEvent("sound_block_step");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_FALL = registerSoundEvent("sound_block_fall");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_PLACE = registerSoundEvent("sound_block_place");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_HIT = registerSoundEvent("sound_block_hit");

    public static final RegistryObject<SoundEvent> RISE = registerSoundEvent("rise");
    public static final RegistryObject<SoundEvent> LIGHT_AND_SHADOW = registerSoundEvent("light_and_shadow");
    public static final RegistryObject<SoundEvent> WARRIORS = registerSoundEvent("warriors");

    public static final RegistryObject<SoundEvent> LINKIN_PARK_IN_THE_END = registerSoundEvent("linkin_park_in_the_end");

    public static final ForgeSoundType SOUND_BLOCK_SOUNDS = new ForgeSoundType(1f, 1f,
            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(SedMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}