// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.api.IMusicLoader;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;

/*
 * DEPRECATED PLEASE USE LoadWAVCommand INSTEAD.
 */
public class LoadCommand implements CommandExecutor, TabCompleter {
	MiniMessage minimessage = MiniMessage.miniMessage();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		IMusicLoader musicLoader = MusicPlayerPlugin.getInstance().getController().getMusicLoader();
		// TODO Auto-generated method stub
		if (args.length != 2) {
			return false;
		}
		if (MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[1])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("musicNameAlreadyInUse").replace("%s", args[1])));
			return true;
		}
		new Thread(() -> {
			try {
				String filepath = Paths.get(MusicPlayerPlugin.getInstance().getMusicPath().toString(), args[0])
						.toString();
				MusicPlayerPlugin.getInstance().getController().getMusicLoader().loadMusic(args[1],
						musicLoader.loadPCMfromFile(filepath));
				new BukkitRunnable() {

					@Override
					public void run() {
						sender.sendMessage(minimessage.deserialize(
								MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message")
										.getString("fileLoadedAs").replace("%s0", args[0]).replace("%s1", args[1])));
					}
				}.runTask(MusicPlayerPlugin.getInstance());

			} catch (IOException e) {
				new BukkitRunnable() {

					@Override
					public void run() {
						sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
								.getConfigurationSection("message").getString("fileNotFound").replace("%s", args[0])));
					}
				}.runTask(MusicPlayerPlugin.getInstance());

			}
		}).start();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			return Stream.of(MusicPlayerPlugin.getInstance().getMusicPath().toFile().listFiles()).map(File::getName)
					.filter(a -> a.startsWith(args[0])).toList();
		}

		if (args.length == 2) {
			return List.of("nom");
		}
		return List.of();

	}

}
