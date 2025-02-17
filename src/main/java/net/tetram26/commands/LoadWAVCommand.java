package net.tetram26.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.audio.MusicLoader;
import net.tetram26.plugin.MusicPlayerPlugin;

public class LoadWAVCommand implements CommandExecutor,TabCompleter{

	

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
	    	MusicLoader loader = MusicPlayerPlugin.getInstance().getAddon().getMusicLoader();
		if (args.length != 2) {
		return false;
		}
		if (MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[1])) {
			sender.sendMessage(Component.text("Le nom '" + args[1] + "' est déjà utilisé!"));
			return true;
		}
		new Thread(() -> {
		try {
			String filepath = Paths.get(MusicPlayerPlugin.getInstance().musicPath.toString(), args[0]).toString();
			MusicPlayerPlugin.getInstance().loadedMusic.put(args[1],loader.loadPCMfromWAV(filepath));
			sender.sendMessage(Component.text("Fichier '"+ args[0]+"' chargé en tant que '"+args[1]+"'"));
		} catch (IOException e) {
			sender.sendMessage(Component.text("Fichier '"+args[0]+"' introuvable"));
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			sender.sendMessage(Component.text("Format de fichier invalide !"));
		}  }).run();
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			return Stream.of(MusicPlayerPlugin.getInstance().musicPath.toFile().listFiles()).map(File::getName).toList();
		}

		if (args.length == 2) {
			return List.of("nom");
		}
		return List.of();
	}
}
