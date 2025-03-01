package net.tetram26.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ListCommand implements CommandExecutor {
	MiniMessage miniMessage = MiniMessage.miniMessage();
	FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();

	@Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	    @NotNull String[] args) {
		if (MusicPlayerPlugin.getInstance().loadedMusic.size() != 0) {
		    sender.sendMessage(miniMessage.deserialize(config.getString("message.listOfLoadedFile")));
		    for (String each : MusicPlayerPlugin.getInstance().loadedMusic.keySet()) {
				sender.sendMessage(Component.text(each));
		    }
		}
		else {
		    sender.sendMessage(miniMessage.deserialize(config.getConfigurationSection("message").getString("noFileLoaded")));
		}
		return true;
    }

}
