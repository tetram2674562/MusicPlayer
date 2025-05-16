package net.tetram26.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.audio.MusicLoader;
import net.tetram26.exceptions.InvalidFileFormatException;
import net.tetram26.plugin.MusicPlayerPlugin;

public class LoadURLCommand implements CommandExecutor, TabCompleter {
	MiniMessage minimessage = MiniMessage.miniMessage();

	// /playmus <URL> <nom>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		MusicLoader loader = MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader();
		if (args.length != 2) {
			return false;
		}
		if (MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader().getAlias().contains(args[1])) {
			sender.sendMessage(Component.text("Le nom '" + args[1] + "' est déjà utilisé!"));
			return true;
		}
		new Thread(() -> {
			try {
				MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader().loadMusic(args[1],
						loader.loadPCMfromURL(args[0]));
				sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message")
						.getString("fileLoadedAs").replace("%s0", args[0]).replace("%s1", args[1])));
			} catch (IOException e) {
				sender.sendMessage(minimessage.deserialize(
						MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message").getString("fileNotFound").replace("%s", args[0])));
			} catch (InvalidFileFormatException e) {
				sender.sendMessage(minimessage
						.deserialize(MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message").getString("invalidFileFormat")));
			} catch (URISyntaxException e) {
				sender.sendMessage(
						minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig().getConfigurationSection("message").getString("invalidURL")));
			}
		}).run();
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
