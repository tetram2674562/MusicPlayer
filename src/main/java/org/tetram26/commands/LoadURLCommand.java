// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.api.IMusicLoader;
import org.tetram26.exceptions.InvalidFileFormatException;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LoadURLCommand implements CommandExecutor, TabCompleter {
	MiniMessage minimessage = MiniMessage.miniMessage();

	// /playmus <URL> <nom>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		IMusicLoader loader = MusicPlayerPlugin.getInstance().getController().getMusicLoader();
		if (args.length != 2) {
			return false;
		}
		if (MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[1])) {
			sender.sendMessage(Component.text("Le nom '" + args[1] + "' est déjà utilisé!"));
			return true;
		}
		new Thread(() -> {
			try {
				MusicPlayerPlugin.getInstance().getController().getMusicLoader().loadMusic(args[1],
						loader.loadPCMfromURL(args[0]));
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

			} catch (InvalidFileFormatException e) {
				new BukkitRunnable() {
					@Override
					public void run() {
						sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
								.getConfigurationSection("message").getString("invalidFileFormat")));
					}
				}.runTask(MusicPlayerPlugin.getInstance());

			} catch (URISyntaxException e) {
				new BukkitRunnable() {

					@Override
					public void run() {
						sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
								.getConfigurationSection("message").getString("invalidURL")));
					}
				}.runTask(MusicPlayerPlugin.getInstance());

			}
		}).start();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return List.of("URL");
		}

		if (args.length == 2) {
			return List.of("nom");
		}
		return List.of();
	}

}
