package net.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.plugin.MusicPlayerPlugin;

public class UnloadCommand implements CommandExecutor, TabCompleter {
    FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();
    MiniMessage minimessage = MiniMessage.miniMessage();

    // Command : /unloadmus name
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	    @NotNull String[] args) {
	if (args.length != 1) {
	    sender.sendMessage(
		    minimessage.deserialize(config.getConfigurationSection("message").getString("invalidArgument")));
	    return false;
	}
	if (!MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[0])) {
	    return false;
	}
	MusicPlayerPlugin.getInstance().loadedMusic.remove(args[0]);
	sender.sendMessage(minimessage.deserialize(
		config.getConfigurationSection("message").getString("unloadedFile").replace("%s", args[0])));
	return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
	    @NotNull String label, @NotNull String[] args) {
	return List.copyOf(MusicPlayerPlugin.getInstance().loadedMusic.keySet());
    }

}
