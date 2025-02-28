package net.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.tetram26.plugin.MusicPlayerPlugin;

public class PauseCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        // TODO Auto-generated method stub
        if (args.length != 1) {
            return false;
        }
        if (!MusicPlayerPlugin.getInstance().activeMusicThread.containsKey(args[0])) {
            sender.sendMessage("Processus '" + args[0] + "' introuvable.");
            return true;
        }
        MusicPlayerPlugin.getInstance().activeMusicThread.get(args[0]).pause();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        // TODO Auto-generated method stub
        if (args.length == 1) {
            return List.copyOf(MusicPlayerPlugin.getInstance().activeMusicThread.keySet());
        }

        return List.of();
    }
}
