package net.tetram26.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ListPlayingCommand implements CommandExecutor {
	MiniMessage miniMessage = MiniMessage.miniMessage();
	FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();

	// Commande : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName().size() != 0) {

			sender.sendMessage(miniMessage.deserialize(config.getString("message.listCurrentlyPlayingMusic")));
			for (String each : MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName()) {
				sender.sendMessage(Component.text(each));
			}
		} else {
			sender.sendMessage(miniMessage
					.deserialize(config.getConfigurationSection("message").getString("noMusicCurrentlyBeingPlayed")));
		}
		return true;
	}

}
