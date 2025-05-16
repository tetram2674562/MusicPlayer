package net.tetram26.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tetram26.addon.MusicAddon;
import net.tetram26.api.ISourceManager;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.player.VoicePlayer;
import su.plo.voice.api.server.player.VoiceServerPlayer;

public class SourceManager implements ISourceManager {

	@Override
	public ServerSourceLine createSourceLine(String name, MusicAddon addon) {
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getSourceLineManager()
				.createBuilder(addon, name, // name
						"pv.activation." + name, // translation key
						"plasmovoice:textures/icons/speaker_priority.png", // icon resource location
						10 // weight
				).build();
		return sourceLine;

	}

	@Override
	public ServerBroadcastSource createBroadcastSource(ServerSourceLine sourceLine, Set<VoicePlayer> voicePlayerList,
			String thread) {
		ServerBroadcastSource source = sourceLine.createBroadcastSource(false);
		source.setPlayers(voicePlayerList);
		// MusicPlayerPlugin.getInstance().broadcastPlayers.put(thread,List.of(source,voicePlayerList,null));
		return source;

	}

	@Override
	public ServerDirectSource createDirectSource(ServerSourceLine sourceLine, String username) {

		VoicePlayer voicePlayer = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
				.getPlayerByName(username).orElseThrow(() -> new IllegalStateException("Player not found"));

		ServerDirectSource source = sourceLine.createDirectSource(voicePlayer, false);

		return source;
	}

	@Override
	public ServerPlayerSource createPlayerSource(ServerSourceLine sourceLine, String username) {
		VoiceServerPlayer voicePlayer = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
				.getPlayerByName(username).orElseThrow(() -> new IllegalStateException("Player not found"));

		ServerPlayerSource source = sourceLine.createPlayerSource(voicePlayer, false);
		source.getFilters().stream().findFirst().ifPresent(source::removeFilter);
		return source;
	}

	@Override
	public Set<VoicePlayer> createPlayerVoiceSet(List<String> playerList) {
		Set<VoicePlayer> voicePlayerList = Collections.synchronizedSet(new HashSet<>());

		for (String each : playerList) {
			VoicePlayer player = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
					.getPlayerByName(each).orElseThrow(() -> new IllegalStateException("Player not found"));
			voicePlayerList.add(player);
		}
		return voicePlayerList;
	}

}