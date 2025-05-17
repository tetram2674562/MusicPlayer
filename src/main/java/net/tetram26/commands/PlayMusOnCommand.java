// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.commands;

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
import net.tetram26.api.IController;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class PlayMusOnCommand implements CommandExecutor, TabCompleter {

	MiniMessage minimessage = MiniMessage.miniMessage();

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias());
		}
		if (args.length == 2) {
			return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
		}
		if (args.length == 3) {
			return List.of("identifiant");
		}
		if (args.length == 4) {
			return List.of("distance");
		}

		return List.of();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		IController controller = MusicPlayerPlugin.getInstance().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		if (args.length != 4) {
			return false;
		}

		if (MusicPlayerPlugin.getInstance().getController().getThreadsName().contains(args[2])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("alreadyUsedThread").replace("%s", args[2])));
			return true;
		}
		if (!MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[0])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("musicNotFound").replace("%s", args[0])));
			return true;
		}
		// <green> Lecture en cours du fichier args[0] en tant que args[2] </green>
		sender.sendMessage(
				minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message")
						.getString("fileBeingPlayed").replace("%s0", args[0]).replace("%s1", args[2])));
		new Thread(() -> {
			try {
				controller.playAudioOn(args[1],
						MusicPlayerPlugin.getInstance().getController().getMusicLoader().getPCMDATA(args[0]),
						sourceLine, args[2], Integer.valueOf(args[3]));
			} catch (NumberFormatException e) {
				sender.sendMessage(minimessage.deserialize("<red>Please give a valid distance number"));
			}
		}).run();
		return true;
	}

}
