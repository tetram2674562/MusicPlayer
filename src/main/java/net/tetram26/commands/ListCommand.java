// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ListCommand implements CommandExecutor {
	MiniMessage miniMessage = MiniMessage.miniMessage();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().size() != 0) {
			sender.sendMessage(miniMessage
					.deserialize(MusicPlayerPlugin.getInstance().getConfig().getString("message.listOfLoadedFile")));
			for (String each : MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias()) {
				sender.sendMessage(Component.text(each));
			}
		} else {
			sender.sendMessage(miniMessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("noFileLoaded")));
		}
		return true;
	}

}
