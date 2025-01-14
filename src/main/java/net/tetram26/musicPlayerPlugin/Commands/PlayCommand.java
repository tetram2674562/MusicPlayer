package net.tetram26.musicPlayerPlugin.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.musicPlayerPlugin.MusicAddon;
import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class PlayCommand implements CommandExecutor,TabCompleter{
	// Commande  : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (args.length != 3) {
			return false;
		}

		if (MusicPlayerPlugin.activeMusicThread.containsKey(args[2])){
			sender.sendMessage(Component.text("L'identifiant '"+args[2]+"' est déjà utilisé par un autre processus"));
			return true;
		}
		if (!MusicPlayerPlugin.loadedMusic.containsKey(args[0])) {
			sender.sendMessage("La musique '"+args[0]+"' est introuvable!");
			return true;
		}
		MusicAddon addon = MusicPlayerPlugin.getAddon();
		new Thread(()-> {
			addon.playAudio(args[1], MusicPlayerPlugin.loadedMusic.get(args[0]),args[2]);
			MusicPlayerPlugin.playerThread.put(args[2], args[1]);
		}).run();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {

		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.loadedMusic.keySet());
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
