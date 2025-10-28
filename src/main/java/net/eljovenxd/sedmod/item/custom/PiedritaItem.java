package net.eljovenxd.sedmod.item.custom;

import net.minecraft.network.chat.Component;
// import net.minecraft.world.entity.player.Player; // Ya no necesitamos esto
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PiedritaItem extends Item {
    public PiedritaItem(Properties pProperties) {
        super(pProperties);
    }

    // 1. Para que brille (como si estuviera encantada)
    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    // 2. Para que no se pueda "romper" o usar (no tiene durabilidad)
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    // 3. Añadir un tooltip que diga que está atascado
    // @Override
    // public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    //     pTooltipComponents.add(Component.translatable("tooltip.sedmod.piedrita.stuck")); // <-- LÍNEA ELIMINADA
    //     super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    // }
    // HEMOS ELIMINADO EL MÉTODO 'appendHoverText' POR COMPLETO

    // 4. ELIMINAMOS el método onDroppedByPlayer
    // @Override
    // public boolean onDroppedByPlayer(ItemStack item, Player player) {
    //     return player.isCreative();
    // }
    // Lo manejaremos 100% con Eventos en ModEvents.java
}