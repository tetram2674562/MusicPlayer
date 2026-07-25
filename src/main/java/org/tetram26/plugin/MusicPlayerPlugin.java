// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.tetram26.addon.MusicAddon;
import org.tetram26.api.IController;
import org.tetram26.api.IMusicPlayerAPI;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.commands.musiccommand.*;
import org.tetram26.commands.basecommand.MusicPlayerCommand;
import org.tetram26.languageHandler.LanguageHandler;
import org.tetram26.listener.BlockListener;
import org.tetram26.listener.ConnectionListener;
import org.tetram26.startup.StartupLoader;

import net.kyori.adventure.text.Component;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public class MusicPlayerPlugin extends JavaPlugin implements IMusicPlayerAPI {

    @Getter
    private LanguageHandler languageHandler;
	@Getter
    private final MusicAddon addon = new MusicAddon();
	@Getter
    private Path configPath = null;
	@Getter
    private Path musicPath = null;
	private final StartupLoader startupLoader = new StartupLoader();


	public static MusicPlayerPlugin getInstance() {
		return getPlugin(MusicPlayerPlugin.class);
	}

	@Override
	public IController getController() {
		return getAddon().getController();
	}

    @Override
	public ServerSourceLine getMusicPlayerSourceLine() {
		return getAddon().getMusicSourceLine();
	}



	@Override
	public void onEnable() {

        if (getServer().getPluginManager().getPlugin("PlasmoVoice") == null) {
            getComponentLogger().error(Component.text("FATAL -- PLASMO VOICE ISN'T INSTALLED ON THIS SERVER."));
            return;
        }
		PlasmoVoiceServer.getAddonsLoader().load(addon);// Init configfiles

		// Registering commands !

		Map<String, MusicCommand> subCommands = new HashMap<>();

		// Loading - unloading commands
		subCommands.put("loadmus", new LoadWAVCommand());
		subCommands.put("loadURL", new LoadURLCommand());
		subCommands.put("unloadmus", new UnloadCommand());

		// Playing commands
		subCommands.put("playmus",new PlayCommand());
		subCommands.put("broadcastmus",new BroadcastCommand());

		// Control commands
		subCommands.put("pausemus",new PauseCommand());
		subCommands.put("resumemus",new ResumeCommand());

		subCommands.put("stopmus",new StopCommand());
		subCommands.put("repeatmus",new RepeatCommand());

		// Listing commands

		subCommands.put("listloaded",new ListCommand());
		subCommands.put("listplaying",new ListPlayingCommand());

		// TODO remake the reload command
		subCommands.put("reload",new ReloadCommand());
		subCommands.put("multiplaymus",new MultiPlayCommand());
		subCommands.put("playmuson",new PlayMusOnCommand());
		subCommands.put("stopallmus",new StopAllCommand());
		subCommands.put("speaker", new SpeakerCommand(addon.getController()));
		MusicPlayerCommand mainCommand = new MusicPlayerCommand(subCommands);

		// Main command
		getCommand("music").setExecutor(mainCommand);
		getCommand("music").setTabCompleter(mainCommand);


		initConfig();
        addon.getController().initSpeakers();

		// Init event listener
		getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(addon.getController().getSpeakerManager(), this), this);

		getComponentLogger().info(Component.text("Hello Server :)"));


	}


	public void initConfig() {
		saveDefaultConfig();
		reloadConfig();
		try {
			// Register config path
			this.configPath = this.getDataPath().toRealPath();

			// Create the dir for the music if it doesn't already exist.
			this.musicPath = Paths.get(getConfigPath().toString(), "music");
			getMusicPath().toFile().mkdir();
			Path startup = startupLoader.getStartupJSONPath("startup.json");
			startupLoader.loadPCMfromJSON(startup.toString());
			this.languageHandler = new LanguageHandler(new File(getDataFolder(), "languages"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ComponentLogger logger() {
		return getInstance().getComponentLogger();
	}
}
