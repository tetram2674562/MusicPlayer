package net.tetram26.musicPlayerPlugin.Audio;

import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.provider.ArrayAudioFrameProvider;
import su.plo.voice.api.server.audio.source.AudioSender;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
// Extracted from plasmo voice wiki (but modified by myself)
public class MusicSender {
	private AudioSender audioSender;
	private ArrayAudioFrameProvider frameProvider;
	/**
     * Sends the audio samples to an audio source in specified distance.
     *
     * @param voiceServer Plasmo Voice Server API.
     * @param source The audio source to send audio.
     * @param samples 48kHz 16-bit mono audio samples.
     */
    public void sendPacketsToDirectSource(
            PlasmoVoiceServer voiceServer,
            ServerDirectSource source,
            short[] samples,
            String threadName
    ) {
        frameProvider = new ArrayAudioFrameProvider(voiceServer, false);

        audioSender = source.createAudioSender(frameProvider);

        frameProvider.addSamples(samples);

        audioSender.start();

        audioSender.onStop(() -> {
            frameProvider.close();

            source.remove();
            // EXPERIMENTAL FEATURE seems to work? (the hell?)
            MusicPlayerPlugin.activeMusicThread.remove(threadName);
        });

    }

    public void sendPacketsToBroadcastSource(
            PlasmoVoiceServer voiceServer,
            ServerBroadcastSource source,
            short[] samples,
            String threadName
    ) {
    	frameProvider = new ArrayAudioFrameProvider(voiceServer, false);

        audioSender = source.createAudioSender(frameProvider);

        frameProvider.addSamples(samples);

        audioSender.start();
        audioSender.onStop(() -> {
            frameProvider.close();
            source.remove();
            // EXPERIMENTAL FEATURE seems to work? (the hell?)
            MusicPlayerPlugin.activeMusicThread.remove(threadName);
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
