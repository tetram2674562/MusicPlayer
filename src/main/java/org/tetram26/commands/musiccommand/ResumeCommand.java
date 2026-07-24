// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands.musiccommand;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class ResumeCommand extends MusicCommand {

	MiniMessage minimessage = MiniMessage.miniMessage();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (!sender.hasPermission("musicplayer.resumemus")) {
			sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
			return true;
		}

		if (args.length < 1) {
			return false;
		}
		if (!MusicPlayerPlugin.getInstance().getController().getThreadsName().contains(args[0])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("threadNotFound")));
			return true;
		}
		MusicPlayerPlugin.getInstance().getController().getThread(args[0]).resume();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getController().getThreadsName().stream()
					.filter(a -> a.startsWith(args[0])).toList());
		}

		return List.of();
	}
}
