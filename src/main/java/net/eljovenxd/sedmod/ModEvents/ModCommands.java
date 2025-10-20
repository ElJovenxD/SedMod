package net.eljovenxd.sedmod.ModEvents;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket; // Asegúrate que esté importado
import net.eljovenxd.sedmod.sleep.SleepStorage;
import net.eljovenxd.sedmod.thirst.ThirstStorage; // Asegúrate que esté importado
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // --- COMANDO DE SED (EXISTENTE) ---
        dispatcher.register(Commands.literal("thirst")
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, 20))
                                .executes(context -> setThirst(context.getSource(), IntegerArgumentType.getInteger(context, "amount")))))
        );

        // --- COMANDO DE FATIGA (NUEVO) ---
        dispatcher.register(Commands.literal("fatigue")
                .then(Commands.literal("get").executes(context -> getFatigue(context.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, 100))
                                .executes(context -> setFatigue(context.getSource(), IntegerArgumentType.getInteger(context, "amount")))))
        );
    }

    // --- MÉTODOS PARA LA SED (EXISTENTES) ---
    private static int setThirst(CommandSourceStack source, int amount) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                thirst.setThirst(amount);
                // Sincronizamos con el cliente para que el HUD se actualice
                ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
                source.sendSuccess(() -> Component.literal("Sed establecida a: " + amount), true);
            });
        }
        return 1;
    }

    // --- MÉTODOS PARA LA FATIGA (NUEVOS) ---
    private static int getFatigue(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getCapability(SleepStorage.SLEEP).ifPresent(sleep -> {
                source.sendSuccess(() -> Component.literal("Tu fatiga actual es: " + sleep.getFatigue()), true);
            });
        }
        return 1;
    }

    private static int setFatigue(CommandSourceStack source, int amount) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getCapability(SleepStorage.SLEEP).ifPresent(sleep -> {
                sleep.setFatigue(amount);
                // Sincronizamos con el cliente para que el HUD se actualice
                ModMessages.sendToPlayer(new SyncFatiguePacket(sleep.getFatigue()), player);
                source.sendSuccess(() -> Component.literal("Fatiga establecida a: " + amount), true);
            });
        }
        return 1;
    }
}
