package net.tetram26.api;

import net.tetram26.controller.Controller;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public interface IMusicPlayerAPI {
	/**
	 * Allow you to get the sound controller
	 * USE IT AT YOUR OWN RISK.
	 *
	 * @return The sound controller for the Music Player API
	 */
	public Controller getController();


	/**
	 * Get the source line from music player
	 * @return The music player dedicated server source line.
	 */
	public ServerSourceLine getMusicPlayerSourceLine();
}
