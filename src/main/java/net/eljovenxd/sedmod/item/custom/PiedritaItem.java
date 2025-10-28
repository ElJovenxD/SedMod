package net.eljovenxd.sedmod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player; // Asegúrate de importar Player
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
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.sedmod.piedrita.stuck"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    // 4. Evita que el jugador lo suelte (Implementación básica)
    //    Devuelve 'false' para cancelar el dropeo.
    //    (Para un bloqueo total en la GUI, necesitaremos Eventos más adelante)
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return false;
    }
}