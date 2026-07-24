// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.commands.musiccommand;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.api.IController;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.plugin.MusicPlayerPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class PlayCommand extends MusicCommand{
	MiniMessage minimessage = MiniMessage.miniMessage();

	// Commande : /playmus <filename> <username> <processus>
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (!sender.hasPermission("musicplayer.playmus")) {
			sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
			return true;
		}

		if (args.length < 1)
			return false;

		String username = args.length == 1 ? sender.getName() : args[1];

		IController controller = MusicPlayerPlugin.getInstance().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		String threadname = args[0] + "_" + username;
		if (MusicPlayerPlugin.getInstance().getController().getThreadsName().contains(threadname)) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("alreadyUsedThread","%s").replace("%s", threadname)));
			return true;
		}
		if (!MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(args[0])) {
			sender.sendMessage(minimessage.deserialize(MusicPlayerPlugin.getInstance().getConfig()
					.getConfigurationSection("message").getString("musicNotFound","%s").replace("%s", args[0])));
			return true;
		}
        new Thread(() -> {
			controller.playAudio(username,
					MusicPlayerPlugin.getInstance().getController().getMusicLoader().getPCMDATA(args[0]), sourceLine,
					threadname);
		}).start();
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
        return switch (args.length ) {
            case 1 -> List.copyOf(MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().stream()
                    .filter(a -> a.startsWith(args[0])).toList());
            case 2 -> Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName)
                    .filter(a -> a.startsWith(args[1])).toList();
            default -> List.of();
        };
	}

}
