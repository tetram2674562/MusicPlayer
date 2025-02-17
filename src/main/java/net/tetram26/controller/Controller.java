package net.tetram26.controller;

import java.util.List;

import net.tetram26.audio.MusicSender;
import net.tetram26.models.SourceManager;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;

public class Controller implements IController{
    
    
    
    public void playAudio(String username,short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
    	MusicSender musicSender = new MusicSender();
    	ServerBroadcastSource musicSource = MusicPlayerPlugin.getInstance().getAddon().getSourceManager().createBroadcastSource(sourceLine,List.of(username),threadName);
    	musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), musicSource, PCMdata,threadName);
    
    }
    
    public void broadcastAudio(List<String> playerList, short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
    	if (playerList.size() != 0) {
    	    MusicSender musicSender = new MusicSender();
    	    ServerBroadcastSource broadcastSource = MusicPlayerPlugin.getInstance().getAddon().getSourceManager().createBroadcastSource(sourceLine,playerList,threadName);
    	    musicSender.sendPacketsToBroadcastSource(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer(), broadcastSource, PCMdata,threadName);
    	    
    	
    	}
    	
    }
}
