package net.tetram26.commands;


import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.tetram26.audio.MusicSender;
import net.tetram26.plugin.MusicPlayerPlugin;

public class StopAllCommand implements CommandExecutor, TabCompleter{

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName)
				.filter(a -> a.startsWith(args[args.length - 1])).toList();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length < 1) {
			return false;
		}
		Set<String> threadsName = MusicPlayerPlugin.getInstance().getController().getThreadsName();
		// For each player in param
		for (String player : args) {
			// For each thread (in parallel)
			threadsName.parallelStream().forEach(s -> {
				MusicSender thread = MusicPlayerPlugin.getInstance().getController().getThread(s);
				if (thread.hasPlayer(player)) {
					thread.stop();
				}
			});
		}
		return true;
	}
	
}
