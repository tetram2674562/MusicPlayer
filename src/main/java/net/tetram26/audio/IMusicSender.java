package net.tetram26.audio;

import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;

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
     * @param playerName
     */
    public void addPlayer(String playerName);
}
