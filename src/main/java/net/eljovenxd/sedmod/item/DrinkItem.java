package net.eljovenxd.sedmod.item;

// --- IMPORTACIONES ---
import net.eljovenxd.sedmod.cocacounter.CocaCounterProvider;
import net.eljovenxd.sedmod.cocacounter.ICocaCounter;
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

                        // Dar la piedrita con la nueva lógica
                        givePiedrita(player);

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

    private void givePiedrita(Player player) {
        ItemStack piedritaStack = new ItemStack(ModItems.PIEDRITA.get());

        // 1. Prioridad MÁXIMA al inventario principal (slots 9-35)
        // Primero, busca un slot vacío
        for (int i = 9; i < 36; i++) {
            if (player.getInventory().getItem(i).isEmpty()) {
                player.getInventory().setItem(i, piedritaStack);
                return;
            }
        }

        // Si no hay slots vacíos, busca un item para reemplazar en el inventario principal
        for (int i = 9; i < 36; i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            if (!currentStack.is(ModItems.PIEDRITA.get())) {
                player.drop(currentStack.copy(), false);
                player.getInventory().setItem(i, piedritaStack);
                return;
            }
        }

        // 2. La Hotbar como ÚLTIMO RECURSO (slots 0-8)
        // Esto solo se ejecuta si el inventario principal (9-35) está lleno de piedritas.
        // Busca un slot vacío en la hotbar
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i).isEmpty()) {
                player.getInventory().setItem(i, piedritaStack);
                return;
            }
        }

        // Si la hotbar está llena, busca un item para reemplazar
        for (int i = 0; i < 9; i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            if (!currentStack.is(ModItems.PIEDRITA.get())) {
                player.drop(currentStack.copy(), false);
                player.getInventory().setItem(i, piedritaStack);
                return;
            }
        }

        // 3. Si todo el inventario está lleno de piedritas, suelta la nueva
        player.drop(piedritaStack, false);
    }
}
