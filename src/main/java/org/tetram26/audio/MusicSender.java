// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.audio;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.tetram26.api.IMusicSender;
import org.tetram26.plugin.MusicPlayerPlugin;

import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.source.AudioSender;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.audio.source.ServerStaticSource;
import su.plo.voice.api.server.player.VoicePlayer;

// Extracted from plasmo voice wiki (but modified by myself)
public class MusicSender implements IMusicSender {
	private volatile AudioSender audioSender;
	// private ArrayAudioFrameProvider frameProvider;
	private volatile MusicAudioFrameProvider frameProvider;
	private Set<String> listPlayers;
	private Set<VoicePlayer> playersVoice;
	private ServerBroadcastSource source;
	@Getter
    private final boolean isBroadcast = true;
	@Getter
    @Setter
    private volatile Location location;
	public MusicSender(List<String> playerList) {
		this.listPlayers = Collections.synchronizedSet(new HashSet<>(listPlayers));
	}
	
	public MusicSender(List<String> listPlayers, Set<VoicePlayer> voicePlayerList, boolean isBroadcast) {

		this.playersVoice = Collections.synchronizedSet(new HashSet<>(voicePlayerList));

		this.listPlayers = Collections.synchronizedSet(new HashSet<>(listPlayers));

	}

    public boolean isLocated() {
        return this.location != null;
    }

	@Override
	public void addPlayer(String playerName) {
		if (isBroadcast && !listPlayers.contains(playerName)) {
			listPlayers.add(playerName);
			playersVoice.add(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
					.getPlayerByName(playerName).orElseThrow(() -> new IllegalStateException("Player not found")));
			source.setPlayers(playersVoice);
		}
	}

	@Override
	public boolean hasPlayer(String playerName) {
		return listPlayers.contains(playerName);
	}

    @Override
	public void pause() {
		frameProvider.pause();
	}

	@Override
	public void resume() {
		frameProvider.resume();
	}

	@Override
	public void sendPacketsToSource(PlasmoVoiceServer voiceServer, ServerBroadcastSource source,
			Supplier<short[]> samples, String threadName) {
		this.source = source;
		frameProvider = new MusicAudioFrameProvider(samples, 2, voiceServer);

		audioSender = source.createAudioSender(frameProvider);

//		frameProvider.addSamples(samples);

		audioSender.start();
		audioSender.onStop(() -> {
			frameProvider.close();
			source.remove();
			frameProvider = null;
			audioSender = null;
			// EXPERIMENTAL FEATURE seems to work? (the hell?)
			MusicPlayerPlugin.getInstance().getController().removeThread(threadName);
		});

	}

	@Override
	public void sendPacketsToSource(PlasmoVoiceServer voiceServer, ServerDirectSource source,
			Supplier<short[]> samples, String threadName) {
		frameProvider = new MusicAudioFrameProvider(samples, 2, voiceServer);

		audioSender = source.createAudioSender(frameProvider);

		audioSender.start();

		audioSender.onStop(() -> {
			frameProvider.close();

			source.remove();
			// EXPERIMENTAL FEATURE seems to work? (the hell?)
			MusicPlayerPlugin.getInstance().getController().removeThread(threadName);
		});

	}

	@Override
	public void sendPacketsToSource(PlasmoVoiceServer voiceServer, ServerPlayerSource source,
			Supplier<short[]> samples, String threadName, short distance) {
		frameProvider = new MusicAudioFrameProvider(samples, 1, voiceServer);

		audioSender = source.createAudioSender(frameProvider, distance);

		// frameProvider.addSamples(samples);

		audioSender.start();
		audioSender.onStop(() -> {
			frameProvider.close();
			source.remove();
			frameProvider = null;
			audioSender = null;
			// EXPERIMENTAL FEATURE seems to work? (the hell?)
			MusicPlayerPlugin.getInstance().getController().removeThread(threadName);
		});

	}
	
	@Override
	public void sendPacketsToSource(PlasmoVoiceServer voiceServer, ServerStaticSource source,
			Supplier<short[]> samples, String threadName, short distance) {
		frameProvider = new MusicAudioFrameProvider(samples, 1, voiceServer);

		audioSender = source.createAudioSender(frameProvider, distance);

		// frameProvider.addSamples(samples);

		audioSender.start();
		audioSender.onStop(() -> {
			frameProvider.close();
			source.remove();
			frameProvider = null;
			audioSender = null;
			// EXPERIMENTAL FEATURE seems to work? (the hell?)
			MusicPlayerPlugin.getInstance().getController().removeThread(threadName);
		});

	}
	
	
	
	@Override
	public void stop() {
		audioSender.stop();
	}

	@Override
	public void toggleRepeat() {
		frameProvider.setLoop(!frameProvider.getLoop());
	}
}
