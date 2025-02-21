package net.tetram26.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	    @NotNull String[] args) {
	if (MusicPlayerPlugin.getInstance().loadedMusic.size() != 0) {
	    sender.sendMessage(Component.text("Liste des fichiers chargés :"));
	    for (String each : MusicPlayerPlugin.getInstance().loadedMusic.keySet()) {
		sender.sendMessage(Component.text(each));
	    }
	} else {
	    sender.sendMessage(Component.text("Aucun fichier n'est actuellement chargé."));
	}
	return true;
    }

}
