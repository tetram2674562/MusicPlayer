package net.tetram26.musicPlayerPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class ListPlayingCommand implements CommandExecutor{
	// Commande  : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (MusicPlayerPlugin.activeMusicThread.size()!=0) {

			sender.sendMessage(Component.text("Liste des musiques en cours de lecture :"));
			for (String each : MusicPlayerPlugin.activeMusicThread.keySet()) {
				sender.sendMessage(Component.text(each));
			}
		}
		else {
			sender.sendMessage(Component.text("Aucune musique n'est actuellement en cours d'écoute"));
		}
		return true;
	}



}
