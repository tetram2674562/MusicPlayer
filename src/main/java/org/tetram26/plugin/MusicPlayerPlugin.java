// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.tetram26.addon.MusicAddon;
import org.tetram26.api.IController;
import org.tetram26.api.IMusicPlayerAPI;
import org.tetram26.commands.BroadcastCommand;
import org.tetram26.commands.ListCommand;
import org.tetram26.commands.ListPlayingCommand;
import org.tetram26.commands.LoadURLCommand;
import org.tetram26.commands.LoadWAVCommand;
import org.tetram26.commands.MultiPlayCommand;
import org.tetram26.commands.MusicPlayerCommand;
import org.tetram26.commands.PauseCommand;
import org.tetram26.commands.PlayCommand;
import org.tetram26.commands.RepeatCommand;
import org.tetram26.commands.ResumeCommand;
import org.tetram26.commands.StopAllCommand;
import org.tetram26.commands.StopCommand;
import org.tetram26.commands.UnloadCommand;
import org.tetram26.listener.ConnectionListener;
import org.tetram26.startup.StartupLoader;

import net.kyori.adventure.text.Component;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class MusicPlayerPlugin extends JavaPlugin implements IMusicPlayerAPI {

	public static MusicPlayerPlugin getInstance() {
		return getPlugin(MusicPlayerPlugin.class);
	}
	private MusicAddon addon = new MusicAddon();
	private Path configPath = null;
	private Path musicPath = null;

	private StartupLoader startupLoader = new StartupLoader();

	public MusicAddon getAddon() {
		return this.addon;
	}

	public Path getConfigPath() {
		return configPath;
	}

	@Override
	public IController getController() {
		return getAddon().getController();
	}

	public InputStream getLanguageIS() {
		return this.getClassLoader().getResourceAsStream("language.toml");
	}

	public Path getMusicPath() {
		return musicPath;
	}

	@Override
	public ServerSourceLine getMusicPlayerSourceLine() {
		return getAddon().getMusicSourceLine();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		saveDefaultConfig();
		reloadConfig();
		sender.sendMessage(getConfig().getRichMessage("reloadConfig"));
		return true;
	}

	@Override
	public void onEnable() {
		PlasmoVoiceServer.getAddonsLoader().load(addon);
		// Registering commands !

		// Loading - unloading commands
		getServer().getPluginCommand("loadmus").setExecutor(new LoadWAVCommand());
		getServer().getPluginCommand("loadmus").setTabCompleter(new LoadWAVCommand());

		getServer().getPluginCommand("loadURL").setExecutor(new LoadURLCommand());
		getServer().getPluginCommand("loadURL").setTabCompleter(new LoadURLCommand());

		getServer().getPluginCommand("unloadmus").setExecutor(new UnloadCommand());
		getServer().getPluginCommand("unloadmus").setTabCompleter(new UnloadCommand());

		// Playing commands

		getServer().getPluginCommand("playmus").setExecutor(new PlayCommand());
		getServer().getPluginCommand("playmus").setTabCompleter(new PlayCommand());

		getServer().getPluginCommand("broadcastmus").setExecutor(new BroadcastCommand());
		getServer().getPluginCommand("broadcastmus").setTabCompleter(new BroadcastCommand());

		// Control commands

		getServer().getPluginCommand("pausemus").setExecutor(new PauseCommand());
		getServer().getPluginCommand("pausemus").setTabCompleter(new PauseCommand());

		getServer().getPluginCommand("resumemus").setExecutor(new ResumeCommand());
		getServer().getPluginCommand("resumemus").setTabCompleter(new ResumeCommand());

		getServer().getPluginCommand("stopmus").setExecutor(new StopCommand());
		getServer().getPluginCommand("stopmus").setTabCompleter(new StopCommand());

		getServer().getPluginCommand("repeatmus").setExecutor(new RepeatCommand());
		getServer().getPluginCommand("repeatmus").setTabCompleter(new RepeatCommand());

		// Listing commands

		getServer().getPluginCommand("listloaded").setExecutor(new ListCommand());

		getServer().getPluginCommand("listplaying").setExecutor(new ListPlayingCommand());

		getServer().getPluginCommand("reloadMusicPlayerConfig").setExecutor(this);
		getCommand("musicplayer").setExecutor(new MusicPlayerCommand());
		getCommand("musicplayer").setTabCompleter(new MusicPlayerCommand());
		getCommand("multiplaymus").setExecutor(new MultiPlayCommand());
		getCommand("multiplaymus").setTabCompleter(new MultiPlayCommand());
		// getCommand("playmuson").setExecutor(new PlayMusOnCommand());
		// getCommand("playmuson").setTabCompleter(new PlayMusOnCommand());

		getCommand("stopallmus").setExecutor(new StopAllCommand());
		getCommand("stopallmus").setTabCompleter(new StopAllCommand());

		// Init event listener
		getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		// Init configfiles

		saveDefaultConfig();

		// Creation of the directory
		// this.getDataFolder().mkdir(); folder automatiquely created with
		// savedefaultconfig
		try {
			// Register config path
			setConfigPath(this.getDataPath().toRealPath());

			// Create the dir for the music if it doesn't already exist.
			setMusicPath(Paths.get(getConfigPath().toString(), "music"));
			getMusicPath().toFile().mkdir();
			Path startup = startupLoader.getStartupJSONPath("startup.json");
			startupLoader.loadPCMfromJSON(startup.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		getComponentLogger().info(Component.text("Hello Server :)"));

	}

	public void setConfigPath(Path configPath) {
		this.configPath = configPath;
	}

	public void setMusicPath(Path musicPath) {
		this.musicPath = musicPath;
	}

}
