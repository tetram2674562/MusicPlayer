package net.tetram26.audio;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.provider.ArrayAudioFrameProvider;
import su.plo.voice.api.server.audio.source.AudioSender;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.player.VoicePlayer;

// Extracted from plasmo voice wiki (but modified by myself)
public class MusicSender implements IMusicSender {
    private AudioSender audioSender;
    private ArrayAudioFrameProvider frameProvider;
    private Set<String> listPlayers;
    private Set<VoicePlayer> playersVoice;
    private ServerBroadcastSource source;
    public boolean isPrivate = true;

    public MusicSender(List<String> listPlayers, Set<VoicePlayer> voicePlayerList) {
	this.listPlayers = Collections.synchronizedSet(new HashSet<>(listPlayers));
	this.playersVoice = Collections.synchronizedSet(new HashSet<>(voicePlayerList));
	if (voicePlayerList.size() > 1) {
	    isPrivate = false;
	}
    }

    public MusicSender(List<String> playerList) {
	this.listPlayers = Collections.synchronizedSet(new HashSet<>(listPlayers));
    }

    @Override
    public void sendPacketsToDirectSource(PlasmoVoiceServer voiceServer, ServerDirectSource source, short[] samples,
	    String threadName) {
	frameProvider = new ArrayAudioFrameProvider(voiceServer, false);

	audioSender = source.createAudioSender(frameProvider);

	frameProvider.addSamples(samples);

	audioSender.start();

	audioSender.onStop(() -> {
	    frameProvider.close();

	    source.remove();
	    // EXPERIMENTAL FEATURE seems to work? (the hell?)
	    MusicPlayerPlugin.getInstance().activeMusicThread.remove(threadName);
	});

    }

    @Override
    public void sendPacketsToBroadcastSource(PlasmoVoiceServer voiceServer, ServerBroadcastSource source,
	    short[] samples, String threadName) {
	this.source = source;
	frameProvider = new ArrayAudioFrameProvider(voiceServer, false);

	audioSender = source.createAudioSender(frameProvider);

	frameProvider.addSamples(samples);

	audioSender.start();
	audioSender.onStop(() -> {
	    frameProvider.close();
	    source.remove();
	    frameProvider = null;
	    // EXPERIMENTAL FEATURE seems to work? (the hell?)
	    MusicPlayerPlugin.getInstance().activeMusicThread.remove(threadName);
	});

    }

    @Override
    public void stop() {
	audioSender.stop();
    }

    @Override
    public void pause() {
	audioSender.pause();
    }

    @Override
    public void resume() {
	audioSender.resume();
    }

    @Override
    public void toggleRepeat() {
	frameProvider.setLoop(!frameProvider.getLoop());
    }

    @Override
    public void addPlayer(String playerName) {
	if (!listPlayers.contains(playerName) && !isPrivate) {
	    listPlayers.add(playerName);
	    playersVoice.add(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
		    .getPlayerByName(playerName).orElseThrow(() -> new IllegalStateException("Player not found")));
	    source.setPlayers(playersVoice);
	}
    }
}
