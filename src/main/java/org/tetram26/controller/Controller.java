// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.controller;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.tetram26.api.IController;
import org.tetram26.audio.MusicLoader;
import org.tetram26.audio.MusicSender;
import org.tetram26.models.SourceManager;
import org.tetram26.plugin.MusicPlayerPlugin;

import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.player.VoicePlayer;

public class Controller implements IController {

	private MusicLoader musicLoader;
	private SourceManager sourceManager;
	private ConcurrentHashMap<String, MusicSender> activeMusicThread = new ConcurrentHashMap<>();

	public Controller() {
		musicLoader = new MusicLoader();
		sourceManager = new SourceManager();
	}

	public boolean addThread(String name, MusicSender sender) {
		boolean existingAlias = activeMusicThread.keySet().contains(name);
		if (!existingAlias) {
			activeMusicThread.put(name, sender);
		}
		return !existingAlias;
	}

	@Override
	public void broadcastAudio(List<String> playerList, Supplier<short[]> PCMdata, ServerSourceLine sourceLine,
			String threadName) {
		if (playerList.size() != 0) {
			Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getController().getSourceManager()
					.createPlayerVoiceSet(playerList);
			MusicSender musicSender = new MusicSender(playerList, voicePlayerList, true);
			ServerBroadcastSource broadcastSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
					.createBroadcastSource(sourceLine, voicePlayerList, threadName);
			musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
					broadcastSource, PCMdata, threadName);
			activeMusicThread.put(threadName, musicSender);

		}

	}

	@Override
	public MusicLoader getMusicLoader() {
		return musicLoader;
	}

	@Override
	public SourceManager getSourceManager() {
		return sourceManager;
	}

	@Override
	public MusicSender getThread(String name) {
		return activeMusicThread.get(name);
	}

	@Override
	public Set<String> getThreadsName() {
		return activeMusicThread.keySet();
	}

	@Override
	public void playAudio(String username, Supplier<short[]> PCMdata, ServerSourceLine sourceLine, String threadName) {
		Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createPlayerVoiceSet(List.of(username));
		MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList, true);
		ServerBroadcastSource musicSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createBroadcastSource(sourceLine, voicePlayerList, threadName);
		musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
				musicSource, PCMdata, threadName);
		activeMusicThread.put(threadName, musicSender);
	}

	@Override
	public void playAudioOn(String username, Supplier<short[]> PCMdata, ServerSourceLine sourceLine, String threadName,
			int distance) {
		Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createPlayerVoiceSet(List.of(username));
		MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList, false);
		ServerPlayerSource musicSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createPlayerSource(sourceLine, username);
		musicSender.sendPacketsToPlayerSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), musicSource,
				PCMdata, threadName, (short) distance);
		activeMusicThread.put(threadName, musicSender);
	}

	@Override
	public boolean removeThread(String name) {
		boolean existingAlias = activeMusicThread.keySet().contains(name);
		if (existingAlias) {
			activeMusicThread.remove(name);
		}
		return existingAlias;
	}

}
