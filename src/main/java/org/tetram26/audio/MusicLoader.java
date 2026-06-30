// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.*;
import org.jspecify.annotations.NonNull;
import org.tetram26.api.IMusicLoader;
import org.tetram26.exceptions.InvalidFileFormatException;
import org.tetram26.plugin.MusicPlayerPlugin;
import org.tetram26.track.Track;

public class MusicLoader implements IMusicLoader {
	private final static AudioFormat plasmoVoiceFormat = new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000,
			false);
	private final static AudioFormat mp3Format = new AudioFormat(44100, 16, 2, true, false);

	private final List<Track> loadedMusic = Collections.synchronizedList(new ArrayList<>());

	@Override
	public short[] byteToShort(byte[] byteData) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian
		short[] shortData = new short[byteData.length / 2];
		for (int i = 0; i < shortData.length; i++) {
			shortData[i] = byteBuffer.getShort();
		}

		return shortData;
	}

	@Override
	public List<String> getAlias() {
		return loadedMusic.parallelStream().map(Track::getName).toList();
	}

	@Override
	public Supplier<short[]> getPCMDATA(String alias) {
		return loadedMusic.parallelStream().filter(track -> track.getName().equals(alias)).toList().getFirst()
				.getAudioData();
	}

	@Override
	public boolean loadMusic(String name, short[] PCMdata) {
		// Check if music with this name already exists
		boolean exists = !loadedMusic.parallelStream().filter(track -> track.getName().equals(name))
                .toList().isEmpty();
		
		if (!exists) {
            MusicPlayerPlugin.logger().info("Loaded file {}", name);
			loadedMusic.add(new Track(name, PCMdata));
			return true; // Successfully loaded
		}
		MusicPlayerPlugin.logger().info("Failed to load file {}", name);
		return false; // Already exists
	}

	@Override
	public short[] loadPCMfromFile(String path) throws IOException {
		short[] pcmData;
		File pcmFile = new File(path);
		
		ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
		try (FileInputStream fis = new FileInputStream(pcmFile)) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				decodedPCM.write(buffer, 0, bytesRead);
			}
		}
		
		pcmData = byteToShort(decodedPCM.toByteArray());
		return pcmData;

	}

	@Override
	public short[] loadPCMfromMP3(String path) throws IOException, InvalidFileFormatException {

		short[] pcmData;
		try {
			InputStream inputStream = new FileInputStream(path);
			ByteArrayOutputStream pcmOutputStream = getByteArrayOutputStream(inputStream);
			// Convert 44110 Khz pcm data to 48 Khz data (hell nah)
			byte[] pcm44110 = pcmOutputStream.toByteArray();
            inputStream.close();
			ByteArrayInputStream bis = new ByteArrayInputStream(pcm44110);

			AudioInputStream ais = new AudioInputStream(bis, mp3Format, AudioSystem.NOT_SPECIFIED);
			AudioInputStream audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, ais);

			ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
			int bytesRead;
		byte[] buffer = new byte[16384];		while ((bytesRead = audioIS.read(buffer)) != -1) {
			decodedPCM.write(buffer, 0, bytesRead);			}

			pcmData = byteToShort(decodedPCM.toByteArray());
            // In case of implosion I put you to trash.
			ais.close();
			audioIS.close();
		} catch (JavaLayerException e) {
			throw new InvalidFileFormatException();
		}
		return pcmData;
	}

	private static @NonNull ByteArrayOutputStream getByteArrayOutputStream(InputStream inputStream) throws BitstreamException, DecoderException {
		Bitstream bitstream = new Bitstream(inputStream);
		Decoder decoder = new Decoder();

		ByteArrayOutputStream pcmOutputStream = new ByteArrayOutputStream();

		Header frameHeader;
		while ((frameHeader = bitstream.readFrame()) != null) {
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frameHeader, bitstream);

			short[] pcm = output.getBuffer();
			for (short sample : pcm) {
				pcmOutputStream.write(sample & 0xff);
				pcmOutputStream.write((sample >> 8) & 0xff);
			}

			bitstream.closeFrame();
			// Help garbage collection on large files
        }
		return pcmOutputStream;
	}

	@Override
	public short[] loadPCMfromURL(String link) throws InvalidFileFormatException, IOException, URISyntaxException {
		short[] pcmData;
		URI uri = new URI(link);
		try {
			AudioInputStream audioIS = AudioSystem.getAudioInputStream(uri.toURL());
			audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, audioIS);
			ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
			int bytesRead;
			byte[] PCM = new byte[16384];
			while ((bytesRead = audioIS.read(PCM)) != -1) {
				decodedPCM.write(PCM, 0, bytesRead);
			}
			pcmData = byteToShort(decodedPCM.toByteArray());
			audioIS.close();
			decodedPCM.close();
		} catch (UnsupportedAudioFileException e) {
			throw new InvalidFileFormatException();
		}
		return pcmData;
	}

	@Override
	public short[] loadPCMfromWAV(String path) throws UnsupportedAudioFileException, IOException {
		short[] pcmData;
		File audioFile = new File(path);

		AudioInputStream audioIS = AudioSystem.getAudioInputStream(audioFile);
		audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, audioIS);
		ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
		int bytesRead;
		byte[] PCM = new byte[16384];
		while ((bytesRead = audioIS.read(PCM)) != -1) {
			decodedPCM.write(PCM, 0, bytesRead);
		}

		pcmData = byteToShort(decodedPCM.toByteArray());
		audioIS.close();
		decodedPCM.close();
		return pcmData;
	}

	

	@Override
	public boolean unloadMusic(String name) {
		boolean existingAlias = loadedMusic.parallelStream().filter(track -> track.getName().equals(name))
				.toArray().length != 0;
		if (existingAlias) {
			loadedMusic.remove(
					loadedMusic.parallelStream().filter(track -> track.getName().equals(name)).toList().getFirst());
		}
		return existingAlias;
	}
}
