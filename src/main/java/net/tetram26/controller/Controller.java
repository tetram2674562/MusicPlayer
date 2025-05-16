package net.tetram26.controller;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.tetram26.api.IController;
import net.tetram26.audio.MusicLoader;
import net.tetram26.audio.MusicSender;
import net.tetram26.models.SourceManager;
import net.tetram26.plugin.MusicPlayerPlugin;
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

	@Override
	public void playAudio(String username, short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
		Set<VoicePlayer> voicePlayerList = Set
				.of(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
						.getPlayerByName(username).orElseThrow(() -> new IllegalStateException("Player not found")));

		MusicSender musicSender = new MusicSender(List.of(username), voicePlayerList);
		ServerBroadcastSource musicSource = MusicPlayerPlugin.getInstance().getAddon().getController()
				.getSourceManager().createBroadcastSource(sourceLine, voicePlayerList, threadName);
		musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
				musicSource, PCMdata, threadName);
		activeMusicThread.put(threadName, musicSender);
	}

	@Override
	public void playAudioOn(String username, short[] PCMdata, ServerSourceLine sourceLine, String threadName,
			int distance) {

		MusicSender musicSender = new MusicSender(List.of(username), null);
		ServerPlayerSource musicSource = MusicPlayerPlugin.getInstance().getAddon().getController().getSourceManager()
				.createPlayerSource(sourceLine, username);
		musicSender.sendPacketsToPlayerSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), musicSource,
				PCMdata, threadName, (short) distance);
		activeMusicThread.put(threadName, musicSender);
	}

	@Override
	public void broadcastAudio(List<String> playerList, short[] PCMdata, ServerSourceLine sourceLine,
			String threadName) {
		if (playerList.size() != 0) {
			Set<VoicePlayer> voicePlayerList = MusicPlayerPlugin.getInstance().getAddon().getController()
					.getSourceManager().createPlayerVoiceSet(playerList);
			MusicSender musicSender = new MusicSender(playerList, voicePlayerList);
			ServerBroadcastSource broadcastSource = MusicPlayerPlugin.getInstance().getAddon().getController()
					.getSourceManager().createBroadcastSource(sourceLine, voicePlayerList, threadName);
			musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(),
					broadcastSource, PCMdata, threadName);
			activeMusicThread.put(threadName, musicSender);

		}

	}

	public boolean addThread(String name, MusicSender sender) {
		boolean existingAlias = activeMusicThread.keySet().contains(name);
		if (!existingAlias) {
			activeMusicThread.put(name, sender);
		}
		return !existingAlias;
	}

	public boolean removeThread(String name) {
		boolean existingAlias = activeMusicThread.keySet().contains(name);
		if (!existingAlias) {
			activeMusicThread.remove(name);
		}
		return existingAlias;
	}

	public MusicSender getThread(String name) {
		return activeMusicThread.get(name);
	}

	public Set<String> getThreadsName() {
		return activeMusicThread.keySet();
	}

	@Override
	public MusicLoader getMusicLoader() {
		return musicLoader;
	}

	@Override
	public SourceManager getSourceManager() {
		return sourceManager;
	}

}
