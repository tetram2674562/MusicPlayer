package net.tetram26.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.tetram26.controller.Controller;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class BroadcastCommand implements CommandExecutor, TabCompleter {
    // Command : /broadcastmus <name> <thread> -> broadcast to all players
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	    @NotNull String[] args) {
		List<String> playerList = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
		Controller controller = MusicPlayerPlugin.getInstance().getAddon().getController();
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
		if (args.length != 2) {
		    return false;
		}

		if (!MusicPlayerPlugin.getInstance().loadedMusic.containsKey(args[0])) {
		    sender.sendMessage(Component.text("Veuillez fournir un nom de fichier audio valide!"));
		    return true;
		}
		if (MusicPlayerPlugin.getInstance().activeMusicThread.containsKey(args[1])) {
		    sender.sendMessage(
			    Component.text("L'identifiant '" + args[1] + "' est déjà utilisé par un autre processus"));
		    return true;
		}

		new Thread(() -> {
		    controller.broadcastAudio(playerList, MusicPlayerPlugin.getInstance().loadedMusic.get(args[0]), sourceLine,
			    args[1]);

		}).run();
		return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
	    @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
		    return List.copyOf(MusicPlayerPlugin.getInstance().loadedMusic.keySet());
		}
		if (args.length == 2) {
		    return List.of("identifiant");
		}

		return List.of();
    }

}
