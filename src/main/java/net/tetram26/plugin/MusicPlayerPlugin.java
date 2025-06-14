// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.tetram26.addon.MusicAddon;
import net.tetram26.api.IController;
import net.tetram26.api.IMusicPlayerAPI;
import net.tetram26.commands.BroadcastCommand;
import net.tetram26.commands.ListCommand;
import net.tetram26.commands.ListPlayingCommand;
import net.tetram26.commands.LoadURLCommand;
import net.tetram26.commands.LoadWAVCommand;
import net.tetram26.commands.MultiPlayCommand;
import net.tetram26.commands.MusicPlayerCommand;
import net.tetram26.commands.PauseCommand;
import net.tetram26.commands.PlayCommand;
import net.tetram26.commands.PlayMusOnCommand;
import net.tetram26.commands.RepeatCommand;
import net.tetram26.commands.ResumeCommand;
import net.tetram26.commands.StopAllCommand;
import net.tetram26.commands.StopCommand;
import net.tetram26.commands.UnloadCommand;
import net.tetram26.listener.ConnectionListener;
import net.tetram26.startup.StartupLoader;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class MusicPlayerPlugin extends JavaPlugin implements IMusicPlayerAPI {

	private MusicAddon addon = new MusicAddon();
	private Path configPath = null;
	private Path musicPath = null;
	private StartupLoader startupLoader = new StartupLoader();

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
		//getCommand("playmuson").setExecutor(new PlayMusOnCommand());
		//getCommand("playmuson").setTabCompleter(new PlayMusOnCommand());
		
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

	public MusicAddon getAddon() {
		return this.addon;
	}

	public static MusicPlayerPlugin getInstance() {
		return getPlugin(MusicPlayerPlugin.class);
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
	public IController getController() {
		return getAddon().getController();
	}

	@Override
	public ServerSourceLine getMusicPlayerSourceLine() {
		return getAddon().getMusicSourceLine();
	}

	public InputStream getLanguageIS() {
		return this.getClassLoader().getResourceAsStream("language.toml");
	}

	public Path getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(Path musicPath) {
		this.musicPath = musicPath;
	}

	public Path getConfigPath() {
		return configPath;
	}

	public void setConfigPath(Path configPath) {
		this.configPath = configPath;
	}

}
