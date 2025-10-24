package net.eljovenxd.sedmod.item.custom;

import net.minecraft.core.BlockPos;
import net.eljovenxd.sedmod.sounds.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class SoundBlock  extends Block {
    public SoundBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        // Esta línea reproduce tu sonido personalizado
        pLevel.playSound(pPlayer, pPos, ModSounds.LINKIN_PARK_IN_THE_END.get(), SoundSource.BLOCKS, 1f, 1f);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.literal("Empieza la musica epica!"));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}