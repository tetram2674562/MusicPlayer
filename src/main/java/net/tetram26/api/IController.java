// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.api;

import java.util.List;

import net.tetram26.audio.MusicLoader;
import net.tetram26.models.SourceManager;
import su.plo.voice.api.server.audio.line.ServerSourceLine;

public interface IController {
	/**
	 * Play an audio at a player
	 *
	 * @param username   The username of the player
	 * @param PCMdata    The PCM data of the audio
	 * @param sourceLine the source line to play the audio on
	 * @param threadName the name of the thread
	 */
	public void playAudio(String username, short[] PCMdata, ServerSourceLine sourceLine, String threadName);

	/**
	 * Play an audio on a given group of player
	 *
	 * @param playerList A list of player
	 * @param PCMdata    The PCM data of the audio
	 * @param sourceLine the source line to play the audio on
	 * @param threadName the name of the thread
	 */
	public void broadcastAudio(List<String> playerList, short[] PCMdata, ServerSourceLine sourceLine,
			String threadName);

	/**
	 * Play an audio on a player and everyone near him
	 *
	 * @param username   The username of the player
	 * @param PCMdata    The PCM data of the audio
	 * @param sourceLine the source line to play the audio on
	 * @param threadName the name of the thread
	 * @param distance   the distance you want people to hear it
	 */
	public void playAudioOn(String username, short[] PCMdata, ServerSourceLine sourceLine, String threadName,
			int distance);

	/**
	 * Get the source manager
	 *
	 * @return The source manager
	 */
	public SourceManager getSourceManager();

	/**
	 * Get the music loader
	 *
	 * @return The music loader
	 */
	public MusicLoader getMusicLoader();

}
