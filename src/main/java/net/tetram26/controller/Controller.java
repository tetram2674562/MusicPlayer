package net.tetram26.controller;

import java.util.List;

import net.tetram26.audio.MusicSender;
import net.tetram26.models.SourceManager;
import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;

public class Controller implements IController{
    private SourceManager sourceManager;
    private PlasmoVoiceServer voiceServer;
    
    public Controller(SourceManager sourceManager, PlasmoVoiceServer server) {
	
	this.sourceManager = sourceManager;
	this.voiceServer = server;
    }
    
    
    public void playAudio(String username,short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
    	MusicSender musicSender = new MusicSender();
    	ServerDirectSource musicSource = sourceManager.createDirectSource(sourceLine,username);
    	musicSender.sendPacketsToDirectSource(voiceServer, musicSource, PCMdata,threadName);
    	MusicPlayerPlugin.activeMusicThread.put(threadName,musicSender);
    
    }
    
    public void broadcastAudio(List<String> playerList, short[] PCMdata, ServerSourceLine sourceLine, String threadName) {
    	MusicSender musicSender = new MusicSender();
    	if (playerList.size() != 0) {
    	ServerBroadcastSource broadcastSource = sourceManager.createBroadcastSource(sourceLine,playerList);
    	musicSender.sendPacketsToBroadcastSource(voiceServer, broadcastSource, PCMdata,threadName);
    	MusicPlayerPlugin.activeMusicThread.put(threadName,musicSender);
    	}
    }
}
