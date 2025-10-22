package net.eljovenxd.sedmod.datagen.loot;

import net.eljovenxd.sedmod.block.ModBlocks;
import net.eljovenxd.sedmod.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.DEEPSLATE_PLASTICO_ORE.get());
        this.dropSelf(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get());
        this.dropSelf(ModBlocks.PLASTICO_ORE.get());
        this.dropSelf(ModBlocks.ALUMINIO_ORE.get());
        //this.dropSelf(ModBlocks.SOUND_BLOCK.get());

        this.add(ModBlocks.PLASTICO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));
        this.add(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));
        this.add(ModBlocks.ALUMINIO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.ALUMINIO_ORE.get(), ModItems.RAW_ALUMINIO.get()));
        this.add(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(), ModItems.RAW_ALUMINIO.get()));

        }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}