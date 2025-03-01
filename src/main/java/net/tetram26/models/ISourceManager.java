package net.tetram26.models;

import java.util.List;
import java.util.Set;

import net.tetram26.addon.MusicAddon;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.player.VoicePlayer;

public interface ISourceManager {
    /**
     * Create a source line
     * 
     * @param name  The name of the source line
     * @param addon The addon
     * @return The newly created source line
     */
    public ServerSourceLine createSourceLine(String name, MusicAddon addon);

    /**
     * Create a broadcast source
     * 
     * @param sourceLine The source line to play sound on
     * @param playerList A list of player
     * @return The newly created broadcast source
     */
    public ServerBroadcastSource createBroadcastSource(ServerSourceLine sourceLine, Set<VoicePlayer> playerList,
	    String thread);

    /**
     * Create a direct source
     * 
     * @param sourceLine The source line to play sound on
     * @param username   The username of the player
     * @return The newly created direct source
     */
    public ServerDirectSource createDirectSource(ServerSourceLine sourceLine, String username);

}
