package net.tetram26.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.plugin.MusicPlayerPlugin;

public class MusicPlayerCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		MiniMessage mm = MiniMessage.miniMessage();
		int size = 0;
		for (String aliasMusic : MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader()
				.getAlias()) {
			// Since this is short It's 2 byte per short.

			size += MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader()
					.getPCMDATA(aliasMusic).length * 2;
		}
		sender.sendMessage(mm.deserialize(
				"Plugin by <red>tetram26</red> with the help of <blue>ht06</blue>\nMemory used by currently loaded music : <red>"
						+ String.valueOf(Math.round((size / 1000000) * 100) / 100) + "</red>Mo"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		return List.of();
	}

}
