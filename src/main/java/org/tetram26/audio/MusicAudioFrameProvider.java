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
	private volatile boolean repeat = false;
	private volatile boolean isPaused = false;
	private final AudioEncoder encoder; // false means mono
	private final Encryption encryption;

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
	public @NotNull AudioFrameResult provide20ms() {
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

		// Always build a full 20ms stereo frame, wrapping if repeat is enabled
		short[] rawFrameData = new short[samplesPer20ms * 2];
		int copied = 0;

		while (copied < rawFrameData.length) {
			int currentPos = position.get();

			if (currentPos >= audioData.length) {
				if (!repeat) {
					return AudioFrameResult.Finished.INSTANCE;
				}
				position.set(0);
				currentPos = 0;
			}

			int remaining = audioData.length - currentPos;
			int toCopy = Math.min(rawFrameData.length - copied, remaining);

			System.arraycopy(audioData, currentPos, rawFrameData, copied, toCopy);
			position.addAndGet(toCopy);
			copied += toCopy;
		}

		// If It's stereo -> just give the data, if It's mono transform it into mono frame
		short[] frameData = channels == 2 ? rawFrameData : stereo2mono(rawFrameData);

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
