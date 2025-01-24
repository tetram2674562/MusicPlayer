package net.tetram26.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import net.tetram26.addon.MusicAddon;
import net.tetram26.audio.MusicSender;
import net.tetram26.commands.BroadcastCommand;
import net.tetram26.commands.ListCommand;
import net.tetram26.commands.ListPlayingCommand;
import net.tetram26.commands.LoadURLCommand;
import net.tetram26.commands.LoadWAVCommand;
import net.tetram26.commands.PauseCommand;
import net.tetram26.commands.PlayCommand;
import net.tetram26.commands.RepeatCommand;
import net.tetram26.commands.ResumeCommand;
import net.tetram26.commands.StopCommand;
import net.tetram26.commands.UnloadCommand;
import net.tetram26.listener.ConnectionListener;
import net.tetram26.startup.StartupLoader;
import su.plo.voice.api.server.PlasmoVoiceServer;

public class MusicPlayerPlugin extends JavaPlugin {

	private final static MusicAddon addon = new MusicAddon();
    public static ConcurrentHashMap<String,short[]> loadedMusic = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,MusicSender> activeMusicThread = new ConcurrentHashMap<>();
    // threadName : playerName 
    public static ConcurrentHashMap<String,String> playerThread = new ConcurrentHashMap<>();
    
    public static Path configPath = null;
    public static Path musicPath = null;
    public static Logger LOGGER;
    
    private StartupLoader startupLoader = new StartupLoader();
    @Override
    public void onEnable() {
		LOGGER = this.getLogger();
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
    	
    	
    	
    	
    	// Init event listener
    	getServer().getPluginManager().registerEvents(new ConnectionListener(),this);
    	// Init configfiles

    	// Creation of the directory
    	this.getDataFolder().mkdir();
    	try {
    	    // Register config path
    	    configPath = this.getDataPath().toRealPath();
    	    // Create the dir for the music if it doesn't already exist.
    	    musicPath = Paths.get(configPath.toString(),"music");
    	    musicPath.toFile().mkdir();
    	    Path startup = startupLoader.getStartupJSONPath("startup.json");
    	    startupLoader.loadPCMfromJSON(startup.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    	LOGGER.info("Hello Server :)");

    }

    public static MusicAddon getAddon() {
		return addon;
	}


}
