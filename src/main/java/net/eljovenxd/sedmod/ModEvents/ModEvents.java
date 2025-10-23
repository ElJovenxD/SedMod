package net.eljovenxd.sedmod.ModEvents;

// --- AÑADIR IMPORTS ---
import net.eljovenxd.sedmod.fatigue.FatigueProvider;
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
// --- FIN IMPORTS ---

import net.eljovenxd.sedmod.item.ModItems;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstProvider;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

@Mod.EventBusSubscriber(modid = "sedmod")
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            // Añade capability de Sed (existente)
            if (!event.getObject().getCapability(ThirstStorage.THIRST).isPresent()) {
                event.addCapability(new ResourceLocation("sedmod", "thirst"), new ThirstProvider());
            }
            // --- AÑADIR ESTE BLOQUE ---
            // Añade capability de Fatiga (nuevo)
            if (!event.getObject().getCapability(FatigueStorage.FATIGUE).isPresent()) {
                event.addCapability(new ResourceLocation("sedmod", "fatigue"), new FatigueProvider());
            }
            // --- FIN DEL BLOQUE ---
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // Copia Sed (existente)
            event.getOriginal().getCapability(ThirstStorage.THIRST).ifPresent(oldStore -> {
                event.getEntity().getCapability(ThirstStorage.THIRST).ifPresent(newStore -> {
                    newStore.setThirst(oldStore.getThirst());
                    newStore.setThirstSaturation(oldStore.getThirstSaturation());
                });
            });

            // --- AÑADIR ESTE BLOQUE ---
            // Copia Fatiga (nuevo)
            event.getOriginal().getCapability(FatigueStorage.FATIGUE).ifPresent(oldStore -> {
                event.getEntity().getCapability(FatigueStorage.FATIGUE).ifPresent(newStore -> {
                    newStore.setFatigue(oldStore.getFatigue());
                });
            });
            // --- FIN DEL BLOQUE ---
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ThirstStorage.register(event); // Existente
        FatigueStorage.register(event); // --- AÑADIR ESTA LÍNEA ---
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Sincroniza Sed (existente)
            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
            });

            // --- AÑADIR ESTE BLOQUE ---
            // Sincroniza Fatiga (nuevo)
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
            // --- FIN DEL BLOQUE ---
        }
    }

    // --- EVENTO MODIFICADO ---
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Lógica de Sed (Existente)
            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                thirst.removeThirst(6); // Quita 30% de sed
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
            });

            // --- AÑADIR ESTE BLOQUE ---
            // Lógica de Fatiga (Nueva)
            player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                fatigue.setFatigue(20); // Restaura la fatiga al máximo
                ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
            });
            // --- FIN DEL BLOQUE ---
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            Player player = event.player;

            // --- Lógica de Sed (Existente) ---
            // Lógica #1: Reducir la sed lentamente (cada 30 segundos)
            if (player.level().getGameTime() % 600 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() > 0) {
                        if (thirst.getThirstSaturation() > 0) {
                            thirst.setThirstSaturation(thirst.getThirstSaturation() - 1.0F);
                        } else {
                            thirst.removeThirst(1);
                        }
                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), (ServerPlayer) player);
                    }
                });
            }

            // Lógica #2: Aplicar daño por sed (cada 4 segundos)
            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                    if (thirst.getThirst() <= 0) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }
                });
            }

            // --- LÓGICA DE FATIGA (NUEVA) ---

            // Lógica #3: Reducir la fatiga lentamente (cada 20 segundos)
            if (player.level().getGameTime() % 400 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    if (fatigue.getFatigue() > 0) {
                        fatigue.removeFatigue(1);
                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), (ServerPlayer) player);
                    }
                });
            }

            // Lógica #4: Aplicar efectos y daño por fatiga (cada 4 segundos)
            if (player.level().getGameTime() % 80 == 0 && !player.isCreative() && !player.isSpectator()) {
                player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                    int fatigueLevel = fatigue.getFatigue();

                    if (fatigueLevel <= 0) {
                        // Daño si la fatiga es 0
                        player.hurt(player.damageSources().starve(), 1.0F);
                    } else if (fatigueLevel <= 6) { // 30% de 20 es 6
                        // Efectos negativos si es 30% o menos
                        // (100 ticks = 5 segundos, se reaplica cada 4s)
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false, true));
                    }
                });
            }
        }
    }

    // (Este evento no necesita cambios, ya que dormir no es un "item")
    @SubscribeEvent
    public static void onPlayerFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        // ... (Sin cambios aquí)
    }
}