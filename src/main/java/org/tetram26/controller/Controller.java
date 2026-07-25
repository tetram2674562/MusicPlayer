// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NonNull;
import org.tetram26.api.IController;
import org.tetram26.audio.MusicLoader;
import org.tetram26.audio.MusicSender;
import org.tetram26.models.SourceManager;
import org.tetram26.plugin.MusicPlayerPlugin;

import org.tetram26.world.SpeakerManager;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.audio.source.ServerStaticSource;
import su.plo.voice.api.server.player.VoicePlayer;

public class Controller implements IController {

	private final MusicLoader musicLoader;
	private final SourceManager sourceManager;
	private final ConcurrentHashMap<String, MusicSender> activeMusicThread = new ConcurrentHashMap<>();
	@Getter
    private SpeakerManager speakerManager;

	public Controller() {
		musicLoader = new MusicLoader();
		sourceManager = new SourceManager();


    }

    public boolean addThread(String name, MusicSender sender) {
		boolean existingAlias = activeMusicThread.containsKey(name);
		if (!existingAlias) {
			activeMusicThread.put(name, sender);
		}
		return !existingAlias;
	}

	@Override
	public void broadcastAudio(List<String> playerList, Supplier<short[]> PCMdata, ServerSourceLine sourceLine,
			String threadName) {
		if (!playerList.isEmpty()) {
			Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getController().getSourceManager()
					.createPlayerVoiceSet(playerList);
			MusicSender musicSender = new MusicSender(playerList, voicePlayerList);
			ServerBroadcastSource broadcastSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
					.createBroadcastSource(sourceLine, voicePlayerList, threadName);
			musicSender.sendPacketsToSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
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
		MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList);
		ServerBroadcastSource musicSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createBroadcastSource(sourceLine, voicePlayerList, threadName);
		musicSender.sendPacketsToSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
				musicSource, PCMdata, threadName);
		activeMusicThread.put(threadName, musicSender);
	}

	@Override
	public void playAudioOn(String username, Supplier<short[]> PCMdata, ServerSourceLine sourceLine, String threadName,
			int distance) {
		Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createPlayerVoiceSet(List.of(username));
		MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList);
		ServerPlayerSource musicSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createPlayerSource(sourceLine, username);
		musicSender.sendPacketsToSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), musicSource,
				PCMdata, threadName, (short) distance);
		activeMusicThread.put(threadName, musicSender);
	}
	@Override
	public String playAudioAt(Location location, String trackName, ServerSourceLine sourceLine, int distance)  {
		if (!musicLoader.isPresent(trackName))
			return "";

		String threadName = UUID.randomUUID().toString();
		MusicSender musicSender = new MusicSender(List.of(), Set.of());
		ServerStaticSource musicSource = MusicPlayerPlugin.getInstance().getController().getSourceManager()
				.createBlockSource(sourceLine, location);
		musicSender.sendPacketsToSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), musicSource,
				musicLoader.getPCMDATA(trackName), threadName , (short) distance);
		musicSender.toggleRepeat();
		musicSender.setLocation(location);
		activeMusicThread.put(threadName, musicSender);
		return threadName;
	}
	@Override
	public boolean removeThread(@NonNull String name) {
		boolean existingAlias = activeMusicThread.containsKey(name);
		if (existingAlias) {
			activeMusicThread.remove(name);
		}
		return existingAlias;
	}
	
	public Optional<MusicSender> checkForMusicThreadAtLocation(Location location) {
		return activeMusicThread.values().stream().filter(MusicSender::isLocated).filter(s -> location.equals(s.getLocation())).findFirst();
	}

	public void removeAllLocatedThread() {
        for (String id : activeMusicThread.keySet()) {
            MusicSender sender = activeMusicThread.get(id);
			if (sender != null && sender.isLocated()) activeMusicThread.remove(id);
        }
    }

	public void stop(String threadName) {
		activeMusicThread.get(threadName).stop();
	}

	public void initSpeakers() {
		speakerManager = new SpeakerManager(this,
				new File(MusicPlayerPlugin.getInstance().getDataFolder(), "locations.yml"),
				new File(MusicPlayerPlugin.getInstance().getDataFolder(), "speakers.yml"));
		try {
			speakerManager.loadLocationsFromFile();
			speakerManager.loadTypeFromFile();
		} catch (IOException ignore) {
			MusicPlayerPlugin.getInstance().getLogger().severe("Impossible to load speakers locations");
		}
	}
}
