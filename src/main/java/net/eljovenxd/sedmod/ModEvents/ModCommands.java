package net.eljovenxd.sedmod.ModEvents;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
// --- IMPORTA LO NECESARIO PARA FATIGA ---
import net.eljovenxd.sedmod.fatigue.FatigueStorage;
import net.eljovenxd.sedmod.networking.SyncFatiguePacket;
// --- FIN IMPORTS ---
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // --- Comando de Sed (EXISTENTE) ---
        dispatcher.register(Commands.literal("thirst")
                .then(Commands.literal("set")
                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20))
                                .executes(command -> {
                                    ServerPlayer player = command.getSource().getPlayerOrException();
                                    int level = IntegerArgumentType.getInteger(command, "level");

                                    player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                                        thirst.setThirst(level);
                                        thirst.setThirstSaturation(level); // Establece saturación también
                                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst(), thirst.getThirstSaturation()), player);
                                        command.getSource().sendSuccess(() -> Component.literal("Nivel de sed establecido a " + level), true);
                                    });

                                    return 1;
                                })
                        )
                )
        );

        // --- Comando de Fatiga (NUEVO) ---
        dispatcher.register(Commands.literal("fatigue") // Nuevo comando base "fatigue"
                .then(Commands.literal("set") // Subcomando "set"
                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20)) // Argumento numérico entre 0 y 20
                                .executes(command -> { // Lo que hace el comando
                                    ServerPlayer player = command.getSource().getPlayerOrException(); // Obtiene el jugador
                                    int level = IntegerArgumentType.getInteger(command, "level"); // Obtiene el número del argumento

                                    // Obtiene la capability de fatiga del jugador
                                    player.getCapability(FatigueStorage.FATIGUE).ifPresent(fatigue -> {
                                        fatigue.setFatigue(level); // Establece el nivel de fatiga
                                        // Envía el paquete para actualizar la barra visual en el cliente
                                        ModMessages.sendToPlayer(new SyncFatiguePacket(fatigue.getFatigue()), player);
                                        // Envía mensaje de confirmación al jugador
                                        command.getSource().sendSuccess(() -> Component.literal("Nivel de fatiga establecido a " + level), true);
                                    });

                                    return 1; // 1 significa que el comando se ejecutó correctamente
                                })
                        )
                )
        );
        // --- FIN Comando de Fatiga ---
    }
}