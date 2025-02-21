package net.tetram26.controller;

import java.util.List;

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
}
