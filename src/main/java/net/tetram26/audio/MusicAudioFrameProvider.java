package net.tetram26.audio;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import su.plo.voice.api.audio.codec.AudioEncoder;
import su.plo.voice.api.audio.codec.CodecException;
import su.plo.voice.api.encryption.Encryption;
import su.plo.voice.api.encryption.EncryptionException;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.provider.AudioFrameProvider;
import su.plo.voice.api.server.audio.provider.AudioFrameResult;

public class MusicAudioFrameProvider implements AudioFrameProvider {

	private final static int SAMPLE_RATE = 48000;
	private final Supplier<short[]> audioDataSupplier;
	private final AtomicInteger position = new AtomicInteger(0);
	private final int channels;
	private boolean repeat = false;
	private boolean isPaused = false;
	private AudioEncoder encoder; // false means mono
	private Encryption encryption;

	public boolean getLoop() {
		return repeat;
	}

	public void setLoop(boolean repeat) {
		this.repeat = repeat;
	}

	public MusicAudioFrameProvider(Supplier<short[]> audioDataSupplier, int channels,
			@NotNull PlasmoVoiceServer voiceServer) {
		this.audioDataSupplier = audioDataSupplier;
		this.channels = channels;
		this.encoder = voiceServer.createOpusEncoder(channels == 2);
		this.encryption = voiceServer.getDefaultEncryption();
	}

	public void close() {
		encoder.close();
	}

	@Override
	public AudioFrameResult provide20ms() {
	    short[] audioData = audioDataSupplier.get();
	    if (audioData == null) {
	        return AudioFrameResult.Finished.INSTANCE;
	    }

	    
	    
	    
	    int samplesPer20ms = (SAMPLE_RATE / 1000) * 20; // 960
	    int requiredSamples = samplesPer20ms * channels; // 960 or 1920

	    
	    
	    if (isPaused) {
	        try {
	            short[] silence = new short[requiredSamples]; // 0-filled
	            byte[] encoded = encoder.encode(silence);
	            byte[] encrypted = encryption.encrypt(encoded);
	            return new AudioFrameResult.Provided(encrypted);
	        } catch (EncryptionException | CodecException e) {
	            e.printStackTrace();
	            return AudioFrameResult.Finished.INSTANCE;
	        }
	    }
	    
	    int currentPos = position.get();

	    if (currentPos >= audioData.length) {
	        if (!repeat) {
	            return AudioFrameResult.Finished.INSTANCE;
	        } else {
	            position.set(0);
	            currentPos = 0;
	        }
	    }

	    // Calculate how many real samples we can take
	    int remaining = audioData.length - currentPos;
	    int samplesToCopy = Math.min(requiredSamples, remaining);

	    short[] frameData = new short[requiredSamples]; // always exact size Opus expects
	    System.arraycopy(audioData, currentPos, frameData, 0, samplesToCopy);

	    // If fewer than needed, rest stays 0 (silence)
	    position.addAndGet(samplesToCopy);

	    try {
	        byte[] encoded = encoder.encode(frameData);
	        byte[] encrypted = encryption.encrypt(encoded);
	        return new AudioFrameResult.Provided(encrypted);
	    } catch (EncryptionException | CodecException e) {
	        e.printStackTrace();
	        return AudioFrameResult.Finished.INSTANCE;
	    }
	}

	public void pause() {
		this.isPaused = true;
	}

	public void resume() {
		this.isPaused = false;
	}
}
