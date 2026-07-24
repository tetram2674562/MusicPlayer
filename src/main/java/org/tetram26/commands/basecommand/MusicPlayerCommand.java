// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands.basecommand;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.plugin.MusicPlayerPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MusicPlayerCommand extends MusicCommand {

    public MusicPlayerCommand(Map<String, MusicCommand> commands) {
        super(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            MiniMessage mm = MiniMessage.miniMessage();
            int size = MusicPlayerPlugin.getInstance().getController().getMusicLoader().getSize();
            sender.sendMessage(mm.deserialize("Plugin by <red>tetram26</red> with the help of <blue>ht06</blue>\nMemory used by currently loaded music : <red>" + Math.round((size / 1000000.) * 100) / 100 + "</red>Mo"));
            return true;
        }

        MusicCommand subCommand = subCommands.get(args[0]);

        return subCommand == null || subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1 && subCommands.get(args[0]) == null)
            return List.of();

        return switch (args.length) {
            case 0 -> new ArrayList<>(subCommands.keySet());
            case 1 -> subCommands.keySet().stream().filter(subCommand -> subCommand.startsWith(args[0])).toList();
            default ->
                    subCommands.get(args[0]).onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        };
    }

}
