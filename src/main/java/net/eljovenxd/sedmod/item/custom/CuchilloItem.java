package net.eljovenxd.sedmod.item.custom;

import net.eljovenxd.sedmod.item.ModItems; // ¡MUY IMPORTANTE importar ModItems!
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

                    // ----- ¡CAMBIO IMPORTANTE! -----
                    // ¡Volvemos a consumir la piedrita!

                    // 1. Eliminar UNA piedrita
                    stackEnSlot.shrink(1); // <-- LÍNEA RESTAURADA
                    // 2. Actualizamos el slot (si queda vacío, se pone EMPTY)
                    pPlayer.getInventory().setItem(i, stackEnSlot.isEmpty() ? ItemStack.EMPTY : stackEnSlot); // <-- LÍNEA RESTAURADA

                    // ----- FIN DEL CAMBIO -----


                    // 3. <<< NUEVO: Reducir la vida máxima permanentemente >>>
                    AttributeInstance maxHealthAttribute = pPlayer.getAttribute(Attributes.MAX_HEALTH);
                    if (maxHealthAttribute != null) {
                        double currentBaseHealth = maxHealthAttribute.getBaseValue();
                        // Restamos 1.0F (medio corazón)
                        double newBaseHealth = currentBaseHealth - 1.0F;

                        // Ponemos un límite mínimo de 2.0F (1 corazón) para que el jugador no muera
                        if (newBaseHealth < 2.0F) {
                            newBaseHealth = 2.0F;
                        }

                        maxHealthAttribute.setBaseValue(newBaseHealth);

                        // Si la vida actual del jugador es mayor que la nueva vida máxima,
                        // la ajustamos para que vea el cambio inmediatamente.
                        if (pPlayer.getHealth() > newBaseHealth) {
                            pPlayer.setHealth((float)newBaseHealth);
                        }
                    }
                    // <<< FIN DE LA NUEVA LÓGICA >>>


                    // 4. Dañar el cuchillo (1 punto de durabilidad)
                    itemInHand.hurtAndBreak(1, pPlayer, (player) -> {
                        player.broadcastBreakEvent(pUsedHand); // Romper si la durabilidad llega a 0
                    });

                    // 5. Sonido de éxito (puedes cambiarlo)
                    pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.PLAYER_HURT, SoundSource.PLAYERS, 1.0F, 1.0F); // Cambiado a sonido de daño
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