// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.api;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.tetram26.audio.MusicSender;
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
	public void playAudio(String username, Supplier<short[]> PCMdata, ServerSourceLine sourceLine, String threadName);

	/**
	 * Play an audio on a given group of player
	 *
	 * @param playerList A list of player
	 * @param PCMdata    The PCM data of the audio
	 * @param sourceLine the source line to play the audio on
	 * @param threadName the name of the thread
	 */
	public void broadcastAudio(List<String> playerList, Supplier<short[]> PCMdata, ServerSourceLine sourceLine,
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
	public void playAudioOn(String username, Supplier<short[]> PCMdata, ServerSourceLine sourceLine, String threadName,
			int distance);

	/**
	 * Get the source manager
	 *
	 * @return The source manager
	 */
	public ISourceManager getSourceManager();

	/**
	 * Get the music loader
	 *
	 * @return The music loader
	 */
	public IMusicLoader getMusicLoader();

	/**
	 * Get a set a all threads name
	 *
	 * @return All active threads names
	 */
	public Set<String> getThreadsName();

	/**
	 * Get the music sender that is running in the thread.
	 *
	 * @param string The thread name
	 * @return The Music Sender that is ran inside the equivalent thread
	 */
	public MusicSender getThread(@NotNull String string);

	/**
	 *
	 * @param string
	 * @return
	 */
	public boolean removeThread(@NotNull String string);

}
