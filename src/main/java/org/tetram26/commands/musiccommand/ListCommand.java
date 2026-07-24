// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands.musiccommand;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class ListCommand extends MusicCommand{
	MiniMessage miniMessage = MiniMessage.miniMessage();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (!sender.hasPermission("musicplayer.listloaded")) {
			sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
			return true;
		}

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

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
		return List.of();
	}
}
