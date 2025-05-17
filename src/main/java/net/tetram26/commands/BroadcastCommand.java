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

public class BroadcastCommand implements CommandExecutor, TabCompleter {

	MiniMessage minimessage = MiniMessage.miniMessage();

	// Command : /broadcastmus <name> <thread> -> broadcast to all players
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		List<String> playerList = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
		IController controller = MusicPlayerPlugin.getInstance().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		if (args.length != 2) {
			return false;
		}

		if (!MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[0])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("musicNotFound")));
			return true;
		}
		if (MusicPlayerPlugin.getInstance().getController().getThreadsName().contains(args[1])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("alreadyUsedThread").replace("%s", args[1])));
			return true;
		}

		new Thread(() -> {
			controller.broadcastAudio(playerList,
					MusicPlayerPlugin.getInstance().getController().getMusicLoader().getPCMDATA(args[0]), sourceLine,
					args[1]);

		}).run();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias());
		}
		if (args.length == 2) {
			return List.of("identifiant");
		}

		return List.of();
	}

}
