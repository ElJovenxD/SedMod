package net.eljovenxd.sedmod.item.custom;

import net.eljovenxd.sedmod.item.ModItems; // ¡MUY IMPORTANTE importar ModItems!
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class CuchilloItem extends SwordItem {
    public CuchilloItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    // Esta es la lógica para "usar" el cuchillo (clic derecho)
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);

        // Buscar la piedrita en el inventario principal
        for (int i = 0; i < pPlayer.getInventory().getContainerSize(); ++i) {
            ItemStack stackEnSlot = pPlayer.getInventory().getItem(i);

            // Si encontramos una piedrita...
            if (stackEnSlot.is(ModItems.PIEDRITA.get())) {

                // Solo ejecutamos la lógica en el servidor
                if (!pLevel.isClientSide) {
                    // 1. Eliminar UNA piedrita
                    stackEnSlot.shrink(1);

                    // 2. Actualizamos el slot (si queda vacío, se pone EMPTY)
                    pPlayer.getInventory().setItem(i, stackEnSlot.isEmpty() ? ItemStack.EMPTY : stackEnSlot);

                    // 3. Dañar el cuchillo (1 punto de durabilidad)
                    itemInHand.hurtAndBreak(1, pPlayer, (player) -> {
                        player.broadcastBreakEvent(pUsedHand); // Romper si la durabilidad llega a 0
                    });

                    // 4. Sonido de éxito (puedes cambiarlo)
                    pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    pPlayer.getCooldowns().addCooldown(this, 20); // Cooldown de 1 seg
                }

                return InteractionResultHolder.success(itemInHand); // Éxito
            }
        }

        // Si no se encontró la piedrita, simplemente se hace el "swing" normal
        return InteractionResultHolder.pass(itemInHand);
    }
}