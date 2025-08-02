package org.tetram26.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tetram26.plugin.MusicPlayerPlugin;

public class ProduceDiskCommand implements CommandExecutor, TabCompleter{

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias();
		}
		return List.of();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (args.length != 1) {
			return true;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null && item.getType().equals(Material.MUSIC_DISC_5)) {
				item.editMeta(meta -> {
				    meta.getPersistentDataContainer().set(new NamespacedKey(MusicPlayerPlugin.getInstance(),"music"), PersistentDataType.STRING, args[0]);
				});
				player.getInventory().setItemInMainHand(item);
			}
		}
		return true;
	}

}
