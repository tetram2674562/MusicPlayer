package net.tetram26.api;

import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;

public interface IMusicSender {
	/**
	 * Sends the audio samples to a player.
	 *
	 *
	 * @param voiceServer Plasmo Voice Server API.
	 * @param source      The audio source to send audio.
	 * @param samples     48kHz 16-bit mono audio samples.
	 * @param threadName  the name of the thread
	 */
	public void sendPacketsToDirectSource(PlasmoVoiceServer voiceServer, ServerDirectSource source, short[] samples,
			String threadName);

	/**
	 * Sends the audio samples to everyone.
	 *
	 *
	 * @param voiceServer Plasmo Voice Server API.
	 * @param source      The audio source to send audio.
	 * @param samples     48kHz 16-bit mono audio samples.
	 * @param threadName  the name of the thread
	 */
	public void sendPacketsToBroadcastSource(PlasmoVoiceServer voiceServer, ServerBroadcastSource source,
			short[] samples, String threadName);

	/**
	 * Sends the audio samples to a player position (and stay on it)
	 *
	 * @param voiceServer Plasmo Voice Server API
	 * @param source      The audio source to send audio.
	 * @param samples     48kHz 16-bit mono audio samples.
	 * @param threadName  the name of the thread
	 * @param distance    the distance
	 */
	public void sendPacketsToPlayerSource(PlasmoVoiceServer voiceServer, ServerPlayerSource source, short[] samples,
			String threadName, short distance);

	/**
	 * Stop the music sender
	 *
	 */
	public void stop();

	/**
	 * Pause the music sender
	 *
	 */
	public void pause();

	/**
	 * Resume the music sender
	 *
	 */
	public void resume();

	/**
	 * Toggle the repeat mode
	 *
	 */
	public void toggleRepeat();

	/**
	 * Add a player to the source.
	 *
	 * @param playerName The player name
	 */
	public void addPlayer(String playerName);

	/**
	 * Check if a player is having music sended to him.
	 *
	 * @param playerName The player name
	 * @return
	 */
	public boolean hasPlayer(String playerName);

}
