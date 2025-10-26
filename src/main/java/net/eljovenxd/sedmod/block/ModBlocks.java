package net.eljovenxd.sedmod.block;

import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.item.custom.SoundBlock;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SedMod.MOD_ID);

    public static final RegistryObject<Block> CAJA_COCA = registryBlock("caja_coca",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> CAJA_PEPSI = registryBlock("caja_pepsi",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> PLASTICO_ORE = registryBlock("plastico_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(1.5f).requiresCorrectToolForDrops(), UniformInt.of(4,7)));

    public static final RegistryObject<Block> DEEPSLATE_PLASTICO_ORE = registryBlock("deepslate_plastico_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(4f).requiresCorrectToolForDrops(), UniformInt.of(4,7)));

    public static final RegistryObject<Block> ALUMINIO_ORE = registryBlock("aluminio_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops(), UniformInt.of(3,6)));


    public static final RegistryObject<Block> DEEPSLATE_ALUMINIO_ORE = registryBlock("deepslate_aluminio_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3,6)));

    public static final RegistryObject<Block> PLASTICO_BLOCK = registryBlock("plastico_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.BONE_BLOCK)));

    public static final RegistryObject<Block> ALUMINIO_BLOCK = registryBlock("aluminio_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.BONE_BLOCK)));

    //Escaleras y demas items del plastico
    public static final RegistryObject<Block> PLASTICO_STAIRS = registryBlock("plastico_stairs",
            () -> new StairBlock(() -> ModBlocks.PLASTICO_BLOCK.get().defaultBlockState(), // <-- CAMBIA ESTO
                    (BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK))));

    public static final RegistryObject<Block> PLASTICO_SLAB = registryBlock("plastico_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK)));

    public static final RegistryObject<Block> PLASTICO_BUTTON = registryBlock("plastico_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON).sound(SoundType.BONE_BLOCK),
                    BlockSetType.IRON, 10, true));

    public static final RegistryObject<Block> PLASTICO_PRESSURE_PLATE = registryBlock("plastico_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK),
                    BlockSetType.IRON));

    public static final RegistryObject<Block> PLASTICO_FENCE = registryBlock("plastico_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK)));

    public static final RegistryObject<Block> PLASTICO_FENCE_GATE = registryBlock("plastico_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK), SoundEvents.CHAIN_PLACE, SoundEvents.ANVIL_BREAK));

    public static final RegistryObject<Block> PLASTICO_WALL = registryBlock("plastico_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK)));

    public static final RegistryObject<Block> PLASTICO_DOOR = registryBlock("plastico_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK), BlockSetType.IRON));

    public static final RegistryObject<Block> PLASTICO_TRAPDOOR = registryBlock("plastico_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.BONE_BLOCK), BlockSetType.IRON));

    //Escaleras y demas items del aluminio

    public static final RegistryObject<Block> ALUMINIO_STAIRS = registryBlock("aluminio_stairs",
            () -> new StairBlock(() -> ModBlocks.ALUMINIO_BLOCK.get().defaultBlockState(),
                    (BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)))); // Copia propiedades de Hierro

    public static final RegistryObject<Block> ALUMINIO_SLAB = registryBlock("aluminio_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> ALUMINIO_BUTTON = registryBlock("aluminio_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON),
                    BlockSetType.IRON, 10, true));

    public static final RegistryObject<Block> ALUMINIO_PRESSURE_PLATE = registryBlock("aluminio_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK),
                    BlockSetType.IRON));

    public static final RegistryObject<Block> ALUMINIO_FENCE = registryBlock("aluminio_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> ALUMINIO_FENCE_GATE = registryBlock("aluminio_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));

    public static final RegistryObject<Block> ALUMINIO_WALL = registryBlock("aluminio_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> ALUMINIO_DOOR = registryBlock("aluminio_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR), BlockSetType.IRON));

    public static final RegistryObject<Block> ALUMINIO_TRAPDOOR = registryBlock("aluminio_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    public static final RegistryObject<Block> SOUND_BLOCK = registryBlock("sound_block",
            () -> new SoundBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
