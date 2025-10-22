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
        // --- ESTILO HIERRO (Aluminio) ---
        // Esto crea el botín que suelta 1 ítem crudo + Fortuna
        this.add(ModBlocks.ALUMINIO_ORE.get(),
                (block) -> createSilkTouchDispatchTable(block,
                        this.applyExplosionDecay(block,
                                LootItem.lootTableItem(ModItems.RAW_ALUMINIO.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        )
                )
        );
        this.add(ModBlocks.DEEPSLATE_ALUMINIO_ORE.get(),
                (block) -> createSilkTouchDispatchTable(block,
                        this.applyExplosionDecay(block,
                                LootItem.lootTableItem(ModItems.RAW_ALUMINIO.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        )
                )
        );

        // --- ESTILO COBRE (Plástico) ---
        // Esto usa la función que suelta 2-5 ítems crudos + Fortuna
        this.add(ModBlocks.PLASTICO_ORE.get(),
                (block) -> createCopperLikeOreDrops(ModBlocks.PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));

        this.add(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(),
                (block) -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_PLASTICO_ORE.get(), ModItems.RAW_PLASTICO.get()));


        // --- OTROS BLOQUES (Que sueltan ellos mismos) ---
        this.dropSelf(ModBlocks.SOUND_BLOCK.get());
        this.dropSelf(ModBlocks.CAJA_COCA.get());
        this.dropSelf(ModBlocks.CAJA_PEPSI.get());
    }

    // ESTA FUNCIÓN ES PARA ESTILO COBRE (2-5 ítems)
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