package org.tetram26.commands.musiccommand;

import java.util.List;
import java.util.Set;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.audio.MusicSender;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.plugin.MusicPlayerPlugin;

public class StopAllCommand extends MusicCommand{

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (!sender.hasPermission("musicplayer.stopallmus")) {
			sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
			return true;
		}

		MiniMessage minimessage = MiniMessage.miniMessage();
		if (args.length < 1) {
            sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
                    .getConfigurationSection("message").getString("invalidArgument")));
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

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName)
				.filter(a -> a.startsWith(args[args.length - 1])).toList();
	}

}
