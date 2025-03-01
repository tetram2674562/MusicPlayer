package net.tetram26.controller;

import java.util.List;
import java.util.Set;

import net.tetram26.audio.MusicSender;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.player.VoicePlayer;

public class Controller implements IController {

    public void playAudio(String username, short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
	Set<VoicePlayer> voicePlayerList = Set
		.of(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
			.getPlayerByName(username).orElseThrow(() -> new IllegalStateException("Player not found")));

	MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList);
	ServerBroadcastSource musicSource = MusicPlayerPlugin.getInstance().getAddon().getSourceManager()
		.createBroadcastSource(sourceLine, voicePlayerList, threadName);
	musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
		musicSource, PCMdata, threadName);
	MusicPlayerPlugin.getInstance().activeMusicThread.put(threadName, musicSender);
    }

    public void broadcastAudio(List<String> playerList, short[] PCMdata, ServerSourceLine sourceLine,
	    String threadName) {
		if (playerList.size() != 0) {
		    Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getAddon().getSourceManager()
			    .createPlayerVoiceSet(playerList);
		    MusicSender musicSender = new MusicSender(playerList, voicePlayerList);
		    ServerBroadcastSource broadcastSource = MusicPlayerPlugin.getInstance().getAddon().getSourceManager()
			    .createBroadcastSource(sourceLine, voicePlayerList, threadName);
		    musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
			    broadcastSource, PCMdata, threadName);
		    MusicPlayerPlugin.getInstance().activeMusicThread.put(threadName, musicSender);

		}

    }
}
