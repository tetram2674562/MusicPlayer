// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.plugin.MusicPlayerPlugin;

public class PauseCommand implements CommandExecutor, TabCompleter {
	MiniMessage minimessage = MiniMessage.miniMessage();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length != 1) {
			return false;
		}
		if (!MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName().contains(args[0])) {
			sender.sendMessage(
					minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message").getString("threadNotFound")));
			return true;
		}
		MusicPlayerPlugin.getInstance().getAddon().getController().getThread(args[0]).pause();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName());
		}

		return List.of();
	}
}
