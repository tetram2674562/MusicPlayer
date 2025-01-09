package net.tetram26.musicPlayerPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.tetram26.musicPlayerPlugin.Audio.MusicSender;
import net.tetram26.musicPlayerPlugin.Audio.Musicloader;
import su.plo.voice.api.addon.AddonInitializer;
import su.plo.voice.api.addon.InjectPlasmoVoice;
import su.plo.voice.api.addon.annotation.Addon;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.player.VoicePlayer;

@Addon(
        // An addon id must start with a lowercase letter and may contain only lowercase letters, digits, hyphens, and underscores.
        // It should be between 4 and 32 characters long.
        id = "pv-addon-music",
        name = "Music addon",
        version = "1.0.0",
        authors = {"tetram26"}
)
public final class MusicAddon implements AddonInitializer{


	@InjectPlasmoVoice
	private PlasmoVoiceServer voiceServer;
	private ServerSourceLine music;
	@Override
	public void onAddonInitialize() {
	    // voiceServer is initialized now
		music = createSourceLine("music");
		
	}

	@Override
	public void onAddonShutdown() {
		// Je le laisse au cas où...
	}
	// DEPREACATED PLEASE USE loadAudioFromWAV INSTEAD.
	public short[] loadAudio(String path) throws IOException {

		short[] PCMData = Musicloader.readPCMfromFile(path);
		return PCMData;
	}

	public short[] loadAudioFromWAV(String path) throws IOException,UnsupportedAudioFileException {

		short[] PCMData = null;
		
		PCMData = Musicloader.loadPCMfromWAV(path);
	
		return PCMData;
	}

	public short[] loadAudioFromURL (String URL) throws UnsupportedAudioFileException, IOException, URISyntaxException {
		short[] PCMData = null;
		PCMData = Musicloader.loadPCMfromURL(URL);
		return PCMData;
	}
	
	public void playAudio(String username,short[] PCMdata, String threadName) {
		MusicSender musicSender = new MusicSender();
		ServerDirectSource musicSource = createDirectSource(music,username);
		musicSender.sendPacketsToDirectSource(voiceServer, musicSource, PCMdata,threadName);
		MusicPlayerPlugin.activeMusicThread.put(threadName,musicSender);

	}
	
	public void broadcastAudio(List<String> playerList, short[] PCMdata, String threadName) {
		MusicSender musicSender = new MusicSender();
		if (playerList.size() != 0) {
		ServerBroadcastSource broadcastSource = createBroadcastSource(music,playerList);
		musicSender.sendPacketsToBroadcastSource(voiceServer, broadcastSource, PCMdata,threadName);
		MusicPlayerPlugin.activeMusicThread.put(threadName,musicSender);
		}
	}


	public ServerDirectSource createDirectSource(ServerSourceLine sourceLine, String username) {

		VoicePlayer voicePlayer = voiceServer.getPlayerManager()
		        .getPlayerByName(username)
		        .orElseThrow(() -> new IllegalStateException("Player not found"));

		ServerDirectSource source = sourceLine.createDirectSource(voicePlayer, false);

		return source;
	}

	public ServerBroadcastSource createBroadcastSource (ServerSourceLine sourceLine, List<String> playerList) {
		ServerBroadcastSource source = sourceLine.createBroadcastSource(false);
		Set<VoicePlayer> voicePlayerList = new HashSet<>();

		for (String each : playerList) {
			VoicePlayer player = voiceServer.getPlayerManager()
				    .getPlayerByName(each)
				    .orElseThrow(() -> new IllegalStateException("Player not found"));
			voicePlayerList.add(player);
		}

		source.setPlayers(voicePlayerList);

		return source;

	}


	private ServerSourceLine createSourceLine(String name) {
		ServerSourceLine sourceLine = voiceServer.getSourceLineManager().createBuilder(
			    this,
			    name, // name
			    "pv.activation."+name, // translation key
			    "plasmovoice:textures/icons/speaker_priority.png", // icon resource location
			    10 // weight
			).build();
		return sourceLine;
	}


}


