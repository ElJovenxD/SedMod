package net.eljovenxd.sedmod.ModEvents;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.eljovenxd.sedmod.networking.ModMessages;
import net.eljovenxd.sedmod.networking.SyncThirstPacket;
import net.eljovenxd.sedmod.thirst.ThirstStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thirst")
                .then(Commands.literal("set")
                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20))
                                .executes(command -> {
                                    ServerPlayer player = command.getSource().getPlayerOrException();
                                    int level = IntegerArgumentType.getInteger(command, "level");

                                    player.getCapability(ThirstStorage.THIRST).ifPresent(thirst -> {
                                        thirst.setThirst(level);
                                        ModMessages.sendToPlayer(new SyncThirstPacket(thirst.getThirst()), player);
                                        command.getSource().sendSuccess(() -> Component.literal("Nivel de sed establecido a " + level), true);
                                    });

                                    return 1;
                                })
                        )
                )
        );
    }
}
