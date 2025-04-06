package net.tetram26.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tetram26.controller.Controller;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class MultiPlayCommand implements CommandExecutor,TabCompleter{

    FileConfiguration config = MusicPlayerPlugin.getInstance().getConfig();
    MiniMessage minimessage = MiniMessage.miniMessage();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        
        if (args.length == 1) {
            return List.copyOf(MusicPlayerPlugin.getInstance().loadedMusic.keySet());
        }        
        return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length < 1) {
            minimessage.deserialize(
		        config.getConfigurationSection("message").getString("musicNotFound").replace("%s", args[2]));
            return true;
        }

        Controller controller = MusicPlayerPlugin.getInstance().getAddon().getController();
	    ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine();
        int i=1;
        List<String> players = List.of();
        while (args.length > i) {
            players.add(args[i]);
        }
        String playersMusic = "";
        for(String name : players) {
            playersMusic = playersMusic + name;
        }

        controller.broadcastAudio(players, MusicPlayerPlugin.getInstance().loadedMusic.get(args[0]), sourceLine, playersMusic);
        return true;
    }

}