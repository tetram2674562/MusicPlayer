package net.tetram26.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.controller.Controller;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class PlayCommand implements CommandExecutor, TabCompleter {
	FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();
	MiniMessage minimessage = MiniMessage.miniMessage();

	// Commande : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		Controller controller = MusicPlayerPlugin.getInstance().getAddon().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		if (args.length != 3) {
			return false;
		}

		if (MusicPlayerPlugin.getInstance().activeMusicThread.containsKey(args[2])) {
			sender.sendMessage(minimessage.deserialize(
					config.getConfigurationSection("message").getString("alreadyUsedThread").replace("%s", args[2])));
			return true;
		}
		if (!MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[0])) {
			sender.sendMessage(minimessage.deserialize(
					config.getConfigurationSection("message").getString("musicNotFound").replace("%s", args[0])));
			return true;
		}
		// <green> Lecture en cours du fichier args[0] en tant que args[2] </green>
		sender.sendMessage(minimessage.deserialize(config.getConfigurationSection("message")
				.getString("fileBeingPlayed").replace("%s0", args[0]).replace("%s1", args[2])));
		new Thread(() -> {
			controller.playAudio(args[1], MusicPlayerPlugin.getInstance().loadedMusic.get(args[0]), sourceLine,
					args[2]);
		}).run();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {

		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.getInstance().loadedMusic.keySet());
		}
		if (args.length == 2) {
			return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
		}
		if (args.length == 3) {
			return List.of("identifiant");
		}

		return List.of();
	}

}
