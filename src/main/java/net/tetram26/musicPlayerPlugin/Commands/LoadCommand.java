package net.tetram26.musicPlayerPlugin.Commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.musicPlayerPlugin.MusicAddon;
import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;


public class LoadCommand implements CommandExecutor,TabCompleter{
	// Commande : /loadogg path name
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		MusicAddon addon = MusicPlayerPlugin.getAddon();
		// TODO Auto-generated method stub
		if (args.length != 2) {
		return false;
		}
		if (MusicPlayerPlugin.loadedMusic.containsKey(args[1])) {
			sender.sendMessage(Component.text("Le nom '" + args[1] + "' est déjà utilisé!"));
			return true;
		}
		new Thread(() -> {
		try {
			String filepath = Paths.get(MusicPlayerPlugin.musicPath.toString(), args[0]).toString();
			MusicPlayerPlugin.loadedMusic.put(args[1],addon.loadAudio(filepath));
			sender.sendMessage(Component.text("Fichier '"+ args[0]+"' chargé en tant que '"+args[1]+"'"));
		} catch (IOException e) {
			sender.sendMessage(Component.text("Fichier '"+args[0]+"' introuvable"));
		}  }).run();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			return Stream.of(MusicPlayerPlugin.musicPath.toFile().listFiles()).map(File::getName).toList();
		}

		if (args.length == 2) {
			return List.of("nom");
		}
		return List.of();

	}

}
