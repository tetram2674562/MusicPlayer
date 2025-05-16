// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.controller.Controller;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class MultiPlayCommand implements CommandExecutor, TabCompleter {

	MiniMessage minimessage = MiniMessage.miniMessage();

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {

		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader().getAlias());
		}
		return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();

	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length < 3) {
			return false;
		}
		if (!MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader().getAlias().contains(args[0])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("musicNotFound")));
			return true;
		}
		if (MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName().contains(args[2])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("alreadyUsedThread").replace("%s", args[2])));
			return true;
		}
		Controller controller = MusicPlayerPlugin.getInstance().getAddon().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		int i = 1;
		List<String> players = new ArrayList<>();
		while (args.length > i) {
			players.add(args[i]);
			i++;
		}

		new Thread(() -> {
			String playersMusic = "";
			for (String name : players) {
				playersMusic = playersMusic + name;
			}
			controller.broadcastAudio(players,
					MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader().getPCMDATA(args[0]),
					sourceLine, playersMusic);
		}).run();
		return true;
	}

}