package net.eljovenxd.sedmod.villager;

import com.google.common.collect.ImmutableSet;
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, SedMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SedMod.MOD_ID);

    public static final RegistryObject<PoiType> COCA_POI = POI_TYPES.register("coca_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CAJA_COCA.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> COCA_MASTER =
            VILLAGER_PROFESSIONS.register("cocamaster", () -> new VillagerProfession("cocamaster",
                    holder -> holder.get() == COCA_POI.get(), holder -> holder.get() == COCA_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));


    public static final RegistryObject<PoiType> PEPSI_POI = POI_TYPES.register("pepsi_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CAJA_PEPSI.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> PEPSI_MASTER =
            VILLAGER_PROFESSIONS.register("pepsimaster", () -> new VillagerProfession("pepsimaster",
                    holder -> holder.get() == PEPSI_POI.get(), holder -> holder.get() == PEPSI_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
