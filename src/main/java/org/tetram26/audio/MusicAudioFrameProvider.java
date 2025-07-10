package org.tetram26.audio;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.tetram26.plugin.MusicPlayerPlugin;

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

	public boolean getLoop() {
		return repeat;
	}

	public void pause() {
		this.isPaused = true;
	}

	@Override
	public AudioFrameResult provide20ms() {
		short[] audioData = audioDataSupplier.get();
		if (audioData == null) {
			return AudioFrameResult.Finished.INSTANCE;
		}

		int samplesPer20ms = (SAMPLE_RATE / 1000) * 20; // 960
		int requiredSamples = samplesPer20ms * channels; // 960 for mono ,1920 for stereo
		
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

		// Calculate how many real samples we can take from stereo audio
		int remaining = audioData.length - currentPos;
		int samplesToCopy = Math.min(samplesPer20ms * 2, remaining);
		// To store the stereo audio 
		short[] rawFrameData = new short[samplesPer20ms * 2]; 
		System.arraycopy(audioData, currentPos, rawFrameData, 0, samplesToCopy);
		// If the audio should be mono
		short[] frameData = new short[requiredSamples];
		// If It's stereo -> just give the data, if It's mono transform it into mono frame
		frameData = channels == 2 ? rawFrameData : stereo2mono(rawFrameData);
			
		 
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

	public void resume() {
		this.isPaused = false;
	}

	public void setLoop(boolean repeat) {
		this.repeat = repeat;
	}
	
	public boolean isStereo() {
		return this.channels == 2;
	}
	
	
	public short[] stereo2mono(short[] stereoPCM) {
		short[] monoPCM = new short[stereoPCM.length / 2];
		for (int i = 0, j = 0; i < stereoPCM.length; i += 2, j++) {
			int left = stereoPCM[i];
			int right = stereoPCM[i + 1];
			monoPCM[j] = (short) ((left + right) / 2);
		}
		return monoPCM;
	}
}
