// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class UnloadCommand implements CommandExecutor, TabCompleter {
	MiniMessage minimessage = MiniMessage.miniMessage();

	// Command : /unloadmus name
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length != 1) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("invalidArgument")));
			return false;
		}
		if (!MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[0])) {
			return false;
		}
		new Thread(() -> {
			MusicPlayerPlugin.getInstance().getController().getMusicLoader().unloadMusic(args[0]);
			new BukkitRunnable() {
				@Override
				public void run() {
					sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
							.getConfigurationSection("message").getString("unloadedFile").replace("%s", args[0])));
				}
			}.runTask(MusicPlayerPlugin.getInstance());
		}).start();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().stream()
					.filter(a -> a.startsWith(args[0])).toList());
		}
		return List.of();
	}

}
