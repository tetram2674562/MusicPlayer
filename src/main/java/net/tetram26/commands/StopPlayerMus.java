/*
 * package net.tetram26.commands;
 * 
 * import java.util.List;
 * 
 * import org.bukkit.Bukkit; import org.bukkit.command.Command; import
 * org.bukkit.command.CommandExecutor; import org.bukkit.command.CommandSender;
 * import org.bukkit.command.TabCompleter; import
 * org.bukkit.configuration.file.FileConfiguration; import
 * org.bukkit.entity.Player; import org.jetbrains.annotations.NotNull; import
 * org.jetbrains.annotations.Nullable;
 * 
 * import net.kyori.adventure.text.minimessage.MiniMessage; import
 * net.tetram26.plugin.MusicPlayerPlugin;
 * 
 * public class StopPlayerMus implements CommandExecutor, TabCompleter {
 * 
 * FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();
 * MiniMessage minimessage = MiniMessage.miniMessage();
 * 
 * @Override public @Nullable List<String> onTabComplete(@NotNull CommandSender
 * sender, @NotNull Command command,
 * 
 * @NotNull String label, @NotNull String[] args) { if (args.length == 1) {
 * return
 * Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
 * } if (args.length == 2) { return
 * List.copyOf(MusicPlayerPlugin.getInstance().getAddon().getController().
 * getMusicLoader().getAlias()); } return List.of(); }
 * 
 * // /stopplayermus <player> <music>
 * 
 * @Override public boolean onCommand(@NotNull CommandSender sender, @NotNull
 * Command command, @NotNull String label,
 * 
 * @NotNull String[] args) { if (args.length != 2) { return false; } String
 * threadname = args[0] + args[1]; if
 * (!MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[1])) {
 * sender.sendMessage(minimessage.deserialize(
 * config.getConfigurationSection("message").getString("musicNotFound").replace(
 * "%s", args[0]))); return true; } if
 * (!MusicPlayerPlugin.getInstance().activeMusicThread.containsKey(threadname))
 * { sender.sendMessage(minimessage.deserialize(
 * config.getConfigurationSection("message").getString("musicNotFound").replace(
 * "%s", threadname)+" pour le joueur " + args[0])); return true; }
 * 
 * 
 * MusicPlayerPlugin.getInstance().activeMusicThread.get(threadname).stop();
 * MusicPlayerPlugin.getInstance().activeMusicThread.remove(threadname);
 * 
 * 
 * 
 * 
 * return true; }
 * 
 * }
 */