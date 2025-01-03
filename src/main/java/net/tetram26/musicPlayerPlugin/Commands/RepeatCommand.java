package net.tetram26.musicPlayerPlugin.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class RepeatCommand implements CommandExecutor,TabCompleter{
	// Commande  : /repeat <musique>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length != 1) {
			return false;
		}
		if (!MusicPlayerPlugin.activeMusicThread.containsKey(args[0])) {
			sender.sendMessage("Processus '"+args[0]+"' introuvable.");
			return true;
		}
		MusicPlayerPlugin.activeMusicThread.get(args[0]).toggleRepeat();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {

		if (args.length == 1) {
			return List.copyOf(MusicPlayerPlugin.activeMusicThread.keySet());
		}
		return List.of();
	}

}
