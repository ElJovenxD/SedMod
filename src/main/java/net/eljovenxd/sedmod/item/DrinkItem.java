package net.eljovenxd.sedmod.item;

// --- IMPORTACIONES ---
import net.eljovenxd.sedmod.SedMod;
import net.eljovenxd.sedmod.cocacounter.CocaCounterProvider;
import net.eljovenxd.sedmod.cocacounter.ICocaCounter;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
// --- FIN IMPORTACIONES ---

public class DrinkItem extends Item {

    // --- SONIDO AL TERMINAR ---
    private final SoundEvent finishSound;

    // Constructor para bebidas sin sonido especial
    public DrinkItem(Properties properties) {
        this(properties, null);
    }

    // Constructor con sonido personalizado
    public DrinkItem(Properties properties, SoundEvent finishSound) {
        super(properties);
        this.finishSound = finishSound;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        // Lógica base (consumir bebida)
        ItemStack result = super.finishUsingItem(pStack, pLevel, pLivingEntity);

        // --- SONIDO DE FINALIZAR ---
        if (this.finishSound != null && !pLevel.isClientSide) {
            pLevel.playSound(
                    null,
                    pLivingEntity.getX(),
                    pLivingEntity.getY(),
                    pLivingEntity.getZ(),
                    this.finishSound,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        // --- LÓGICA DEL CONTADOR DE COCA ---
        if (pLivingEntity instanceof Player player && !pLevel.isClientSide) {
            if (pStack.is(ModItems.COCA.get())) {
                LazyOptional<ICocaCounter> cocaCounterOptional =
                        player.getCapability(CocaCounterProvider.COCA_COUNTER_CAPABILITY);

                cocaCounterOptional.ifPresent(cocaCounter -> {
                    cocaCounter.addCocaCounter(1);
                    int currentCount = cocaCounter.getCocaCounter();

                    if (currentCount >= 10) {
                        cocaCounter.setCocaCounter(0);

                        // Dar la piedrita
                        player.getInventory().add(new ItemStack(ModItems.PIEDRITA.get()));

                        // Mensaje
                        player.sendSystemMessage(Component.translatable("message.sedmod.piedrita_obtained"));

                        // Sonido divertido al ganar la piedrita
                        pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 1.0f, 0.8f);
                    }
                });
            }
        }

        return result;
    }
}
