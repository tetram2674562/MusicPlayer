package net.tetram26.audio;

import net.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.provider.ArrayAudioFrameProvider;
import su.plo.voice.api.server.audio.source.AudioSender;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
// Extracted from plasmo voice wiki (but modified by myself)
public class MusicSender implements IMusicSender {
    private AudioSender audioSender;
    private ArrayAudioFrameProvider frameProvider;
    private ServerBroadcastSource source;
    
    public void sendPacketsToDirectSource(
            PlasmoVoiceServer voiceServer,
            ServerDirectSource source,
            short[] samples,
            String threadName)
    {
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

    public void sendPacketsToBroadcastSource(
            PlasmoVoiceServer voiceServer,
            ServerBroadcastSource source,
            short[] samples,
            String threadName) 
    {
	this.source = source;
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

    public void stop() {
    	audioSender.stop();
    }
    public void pause() {
    	audioSender.pause();
    }
    public void resume() {
    	audioSender.resume();
    }
    public void toggleRepeat() {
    	frameProvider.setLoop(!frameProvider.getLoop());
    }

}
