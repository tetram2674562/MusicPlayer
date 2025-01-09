package net.tetram26.musicPlayerPlugin.Commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.musicPlayerPlugin.MusicAddon;
import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class LoadURLCommand implements CommandExecutor, TabCompleter{
	//    /playmus <URL> <nom>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		// TODO Auto-generated method stub
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
			MusicPlayerPlugin.loadedMusic.put(args[1],addon.loadAudioFromURL(args[0]));
			sender.sendMessage(Component.text("Fichier '"+ args[0]+"' chargé en tant que '"+args[1]+"'"));
		} catch (IOException e) {
			sender.sendMessage(Component.text("Fichier '"+args[0]+"' introuvable"));
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			sender.sendMessage(Component.text("Format de fichier invalide !"));
		} catch (URISyntaxException e) {
			sender.sendMessage(Component.text(""));
		}  }).run();
		return true;
	}
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			return List.of("URL");
		}

		if (args.length == 2) {
			return List.of("nom");
		}
		return List.of();
	}


}
