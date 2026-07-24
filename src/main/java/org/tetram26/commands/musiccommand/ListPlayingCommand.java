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

public class ListPlayingCommand extends MusicCommand {
	MiniMessage miniMessage = MiniMessage.miniMessage();

	// Commande : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (!sender.hasPermission("musicplayer.listplaying")) {
			sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
			return true;
		}


		if (!MusicPlayerPlugin.getInstance().getController().getThreadsName().isEmpty()) {

			sender.sendMessage(miniMessage.deserialize(
					MusicPlayerPlugin.getInstance().getConfig().getString("message.listCurrentlyPlayingMusic","")));
			for (String each : MusicPlayerPlugin.getInstance().getController().getThreadsName()) {
				sender.sendMessage(Component.text(each));
			}
		} else {
			sender.sendMessage(miniMessage.deserialize(MusicPlayerPlugin.getInstance().getConfig().getString("message.noMusicCurrentlyBeingPlayed","")));
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
		return List.of();
	}
}
