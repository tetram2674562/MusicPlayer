// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class MusicPlayerCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		MiniMessage mm = MiniMessage.miniMessage();
		int size = 0;
		sender.sendMessage(mm.deserialize(
				"Plugin by <red>tetram26</red> with the help of <blue>ht06</blue>\nMemory used by currently loaded music : <red>"
						+ String.valueOf(Math.round((size / 1000000) * 100) / 100) + "</red>Mo"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		return List.of();
	}

}
