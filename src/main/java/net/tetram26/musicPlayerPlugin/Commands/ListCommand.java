package net.tetram26.musicPlayerPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class ListCommand implements CommandExecutor{

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (MusicPlayerPlugin.loadedMusic.size()!=0) {
			sender.sendMessage(Component.text("Liste des fichiers chargés :"));
			for (String each : MusicPlayerPlugin.loadedMusic.keySet()) {
				sender.sendMessage(Component.text(each));
			}
		}
		else {
			sender.sendMessage(Component.text("Aucun fichier n'est actuellement chargé."));
		}
		return true;
	}

}
