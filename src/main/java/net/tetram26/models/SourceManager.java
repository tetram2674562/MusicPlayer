package net.tetram26.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tetram26.addon.MusicAddon;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.player.VoicePlayer;

public class SourceManager implements ISourceManager{
    
    	
    	public SourceManager(){
    	    
    	}
    	
	public ServerSourceLine createSourceLine(String name,MusicAddon addon) {
		ServerSourceLine sourceLine = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getSourceLineManager().createBuilder(
			    addon,
			    name, // name
			    "pv.activation."+name, // translation key
			    "plasmovoice:textures/icons/speaker_priority.png", // icon resource location
			    10 // weight
			).build();
		return sourceLine;
	
	}
	
	public ServerBroadcastSource createBroadcastSource (ServerSourceLine sourceLine, List<String> playerList,String thread) {
		ServerBroadcastSource source = sourceLine.createBroadcastSource(false);
		Set<VoicePlayer> voicePlayerList = new HashSet<>();

		for (String each : playerList) {
			VoicePlayer player = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
				    .getPlayerByName(each)
				    .orElseThrow(() -> new IllegalStateException("Player not found"));
			voicePlayerList.add(player);
		}

		source.setPlayers(voicePlayerList);
		MusicPlayerPlugin.getInstance().broadcastPlayers.put(thread,List.of(source,voicePlayerList,playerList));
		return source;

	}
	
	public ServerDirectSource createDirectSource(ServerSourceLine sourceLine, String username) {

		VoicePlayer voicePlayer = MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getPlayerManager()
		        .getPlayerByName(username)
		        .orElseThrow(() -> new IllegalStateException("Player not found"));

		ServerDirectSource source = sourceLine.createDirectSource(voicePlayer, false);

		return source;
	}
	
	
	
}