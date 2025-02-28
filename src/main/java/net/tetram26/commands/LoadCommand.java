package net.tetram26.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.audio.MusicLoader;
import net.tetram26.plugin.MusicPlayerPlugin;

/*
 * DEPRECATED PLEASE USE LoadWAVCommand INSTEAD.
 */
public class LoadCommand implements CommandExecutor, TabCompleter {
	FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();
	MiniMessage minimessage = MiniMessage.miniMessage();
    // Commande : /loadogg path name
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	    @NotNull String[] args) {
		MusicLoader musicLoader = MusicPlayerPlugin.getInstance().getAddon().getMusicLoader();
		// TODO Auto-generated method stub
		if (args.length != 2) {
		    return false;
		}
		if (MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[1])) {
		    sender.sendMessage(minimessage.deserialize(config.getString("message.musicNameAlreadyInUse").replace("%s", args[1])));
		    return true;
		}
		new Thread(() -> {
		    try {
				String filepath = Paths.get(MusicPlayerPlugin.getInstance().musicPath.toString(), args[0]).toString();
				MusicPlayerPlugin.getInstance().loadedMusic.put(args[1], musicLoader.loadPCMfromFile(filepath));
				sender.sendMessage(minimessage.deserialize(config.getString("message.fileLoadedAs").replace("%s0", args[0]).replace("%s1", args[1])));
		    }
			catch (IOException e) {
				sender.sendMessage(minimessage.deserialize(config.getString("message.fileNotFound").replace("%s", args[0])));
		    }
		}).run();
		return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
	    @NotNull String label, @NotNull String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
		    return Stream.of(MusicPlayerPlugin.getInstance().musicPath.toFile().listFiles()).map(File::getName)
			    .toList();
		}

		if (args.length == 2) {
		    return List.of("nom");
		}
		return List.of();

    }

}
