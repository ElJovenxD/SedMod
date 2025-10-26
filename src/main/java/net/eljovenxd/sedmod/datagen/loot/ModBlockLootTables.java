package net.eljovenxd.sedmod.datagen.loot;

import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        // --- Bloques Básicos (Que sueltan ellos mismos) ---
        this.dropSelf(ModBlocks.SOUND_BLOCK.get());
        this.dropSelf(ModBlocks.CAJA_COCA.get());
        this.dropSelf(ModBlocks.CAJA_PEPSI.get());
        this.dropSelf(ModBlocks.PLASTICO_BLOCK.get());
        this.dropSelf(ModBlocks.ALUMINIO_BLOCK.get());

        // --- Menas de Aluminio (Estilo Hierro) ---
        this.add(ModBlocks.ALUMINIO_ORE.get(),
                block -> createSilkTouchDispatchTable(block,
                        this.applyExplosionDecay(block,
                                LootItem.lootTableItem(ModItems.RAW_ALUMINIO.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        )
                )
        );
        this.add(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(),
                block -> createSilkTouchDispatchTable(block,
                        this.applyExplosionDecay(block,
                                LootItem.lootTableItem(ModItems.RAW_ALUMINIO.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        )
                )
        );

        // --- Menas de Plástico (Estilo Cobre, como el ejemplo) ---
        this.add(ModBlocks.PLASTICO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));
        this.add(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));


        // --- Set de Plástico (Como el ejemplo de Sapphire) ---
        this.dropSelf(ModBlocks.PLASTICO_STAIRS.get());
        this.dropSelf(ModBlocks.PLASTICO_BUTTON.get());
        this.dropSelf(ModBlocks.PLASTICO_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.PLASTICO_TRAPDOOR.get());
        this.dropSelf(ModBlocks.PLASTICO_FENCE.get());
        this.dropSelf(ModBlocks.PLASTICO_FENCE_GATE.get());
        this.dropSelf(ModBlocks.PLASTICO_WALL.get());

        this.add(ModBlocks.PLASTICO_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.PLASTICO_SLAB.get()));
        this.add(ModBlocks.PLASTICO_DOOR.get(),
                block -> createDoorTable(ModBlocks.PLASTICO_DOOR.get()));

        // --- Set de Aluminio (Como el ejemplo de Sapphire) ---
        this.dropSelf(ModBlocks.ALUMINIO_STAIRS.get());
        this.dropSelf(ModBlocks.ALUMINIO_BUTTON.get());
        this.dropSelf(ModBlocks.ALUMINIO_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.ALUMINIO_TRAPDOOR.get());
        this.dropSelf(ModBlocks.ALUMINIO_FENCE.get());
        this.dropSelf(ModBlocks.ALUMINIO_FENCE_GATE.get());
        this.dropSelf(ModBlocks.ALUMINIO_WALL.get());

        this.add(ModBlocks.ALUMINIO_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.ALUMINIO_SLAB.get()));
        this.add(ModBlocks.ALUMINIO_DOOR.get(),
                block -> createDoorTable(ModBlocks.ALUMINIO_DOOR.get()));
    }

    // --- Helper de Estilo Cobre (Igual que el ejemplo) ---
    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(pItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}