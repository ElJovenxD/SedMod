package net.eljovenxd.sedmod.item;

// --- AÑADE ESTAS IMPORTACIONES ---
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
// --- FIN DE IMPORTACIONES ---

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class DrinkItem extends Item {

    // --- AÑADE ESTE CAMPO ---
    private final SoundEvent finishSound;
    // --- FIN DEL CAMPO ---

    // Constructor original (para items sin sonido al terminar, como PEPSI o AGUA)
    public DrinkItem(Properties properties) {
        this(properties, null); // Llama al nuevo constructor con sonido nulo
    }

    // --- AÑADE ESTE NUEVO CONSTRUCTOR ---
    public DrinkItem(Properties properties, SoundEvent finishSound) {
        super(properties);
        this.finishSound = finishSound; // Asigna el sonido
    }
    // --- FIN DEL CONSTRUCTOR ---

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    // --- AÑADE ESTE MÉTODO ---
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        // Llama al método original para aplicar efectos de comida/bebida
        ItemStack result = super.finishUsingItem(pStack, pLevel, pEntityLiving);

        // Si este item tiene un sonido asignado y estamos en el servidor
        if (this.finishSound != null && !pLevel.isClientSide) {
            pLevel.playSound(
                    null, // 'null' para que todos lo escuchen
                    pEntityLiving.getX(),
                    pEntityLiving.getY(),
                    pEntityLiving.getZ(),
                    this.finishSound, // Usa el sonido guardado
                    SoundSource.PLAYERS,
                    1.0F, // Volumen
                    1.0F  // Tono (pitch)
            );
        }

        return result;
    }
    // --- FIN DEL MÉTODO ---
}