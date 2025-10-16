package net.eljovenxd.sedmod.ModEvents;

import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstProvider;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

@Mod.EventBusSubscriber(modid = "sedmod")
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ThirstStorage.THIRST).isPresent()) {
                event.addCapability(new ResourceLocation("sedmod", "thirst"), new ThirstProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ThirstStorage.THIRST).ifPresent(oldStore -> {
                event.getOriginal().getCapability(ThirstStorage.THIRST).ifPresent(newStore -> {
                    newStore.setThirst(oldStore.getThirst());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ThirstStorage.register(event);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            Player player = event.player;
            // --- BAJA LA SED MÁS DESPACIO (CADA 30 SEGUNDOS) ---
            if (player.level().getGameTime() % 600 == 0) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    // --- NUEVA LÓGICA DE SATURACIÓN ---
                    if (thirst.getThirstSaturation() > 0) {
                        // Si hay saturación, se gasta primero.
                        thirst.setThirstSaturation(thirst.getThirstSaturation() - 1.0F);
                    } else if (thirst.getThirst() > 0) {
                        // Si no hay saturación, baja la sed.
                        thirst.removeThirst(1);
                    } else {
                        // Si la sed es cero, el jugador recibe daño.
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }
                    // Sincroniza los datos con el cliente.
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player && !event.getEntity().level().isClientSide()) {
            Player player = (Player) event.getEntity();
            ItemStack itemStack = event.getItem();

            if (itemStack.is(ModItems.AGUA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(5);
                    thirst.addThirstSaturation(6.0F); // <-- AÑADE SATURACIÓN
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.COCA.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(15);
                    thirst.addThirstSaturation(12.0F); // <-- AÑADE SATURACIÓN
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            } else if (itemStack.is(ModItems.PEPSI.get())) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    thirst.addThirst(10);
                    thirst.addThirstSaturation(9.0F); // <-- AÑADE SATURACIÓN
                    ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                });
            }
        }
    }
}