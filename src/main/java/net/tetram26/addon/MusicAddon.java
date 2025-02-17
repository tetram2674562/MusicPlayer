package net.tetram26.addon;

import net.tetram26.controller.Controller;
import net.tetram26.models.SourceManager;
import net.tetram26.audio.MusicLoader;
import su.plo.voice.api.addon.AddonInitializer;
import su.plo.voice.api.addon.InjectPlasmoVoice;
import su.plo.voice.api.addon.annotation.Addon;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

@Addon(
        // An addon id must start with a lowercase letter and may contain only lowercase letters, digits, hyphens, and underscores.
        // It should be between 4 and 32 characters long.
        id = "pv-addon-music",
        name = "Music addon",
        version = "1.0.2",
        authors = {"tetram26"}
)
public final class MusicAddon implements AddonInitializer{


	@InjectPlasmoVoice
	private PlasmoVoiceServer voiceServer;
	private ServerSourceLine music;
	private MusicLoader musicLoader;
	private SourceManager sourceManager;
	private Controller controller;
	@Override
	public void onAddonInitialize() {
	    // voiceServer is initialized now
	    musicLoader = new MusicLoader();
	    sourceManager = new SourceManager();
	    music = sourceManager.createSourceLine("music",this);
	    controller = new Controller();
	   
	}

	@Override
	public void onAddonShutdown() {
		// Je le laisse au cas où...
	}
	
	/** Get the voice server.
	 * 
	 * @return
	 */
	public PlasmoVoiceServer getVoiceServer() {
	    return voiceServer;
	}
	
	/**
	 * Get the music source line
	 * @return The music source line
	 */
	public ServerSourceLine getMusicSourceLine() {
	    return music;
	}
	
	/**
	 * Get the music loader
	 * @return The music loader
	 */
	public MusicLoader getMusicLoader() {
	    return musicLoader;
	}
	
	/**
	 * Get the music controller
	 * @return The music controller
	 */
	public Controller getController() {
	    return controller;
	}
	
	/**
	 * Get the source manager
	 * @return The source manager
	 */
	public SourceManager getSourceManager() {
	    return sourceManager;
	}
	
	

}


