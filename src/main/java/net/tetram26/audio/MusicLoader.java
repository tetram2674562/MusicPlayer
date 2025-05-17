// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.audio;

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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import net.tetram26.api.IMusicLoader;
import net.tetram26.exceptions.InvalidFileFormatException;

public class MusicLoader implements IMusicLoader {
	private final static AudioFormat plasmoVoiceFormat = new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000,
			false);
	private final static AudioFormat mp3Format = new AudioFormat(44100, 16, 2, true, false);

	private ConcurrentHashMap<String, short[]> loadedMusic = new ConcurrentHashMap<>();

	@Override
	public short[] loadPCMfromWAV(String path) throws UnsupportedAudioFileException, IOException {
		short[] pcmData = null;
		File audioFile = new File(path);

		AudioInputStream audioIS = AudioSystem.getAudioInputStream(audioFile);
		audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, audioIS);
		ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
		int bytesRead;
		byte[] PCM = new byte[2];
		while ((bytesRead = audioIS.read(PCM)) != -1) {
			decodedPCM.write(PCM, 0, bytesRead);
		}

		pcmData = byteToShort(decodedPCM.toByteArray());
		audioIS.close();
		decodedPCM.close();
		return pcmData;
	}

	@Override
	public short[] loadPCMfromFile(String path) throws IOException {
		short[] pcmData;
		// Read ALL bytes from the file
		File pcmFile = new File(path);
		byte[] pcmBytes = Files.readAllBytes(pcmFile.toPath());

		// Convert byte[] to short[]
		pcmData = byteToShort(pcmBytes);
		return pcmData;

	}

	@Override
	public short[] loadPCMfromURL(String link) throws InvalidFileFormatException, IOException, URISyntaxException {
		short[] pcmData = null;
		URI uri = new URI(link);
		try {
			AudioInputStream audioIS = AudioSystem.getAudioInputStream(uri.toURL());
			audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, audioIS);
			ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
			int bytesRead;
			byte[] PCM = new byte[2];
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
	public short[] loadPCMfromMP3(String path) throws IOException, InvalidFileFormatException {

		short[] pcmData = null;
		try {
			InputStream inputStream = new FileInputStream(path);
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
			}
			// Convert 44110 Khz pcm data to 48 Khz data (hell nah)
			byte[] pcm44110 = pcmOutputStream.toByteArray();
			pcmOutputStream = null;
			inputStream.close();
			ByteArrayInputStream bis = new ByteArrayInputStream(pcm44110);

			AudioInputStream ais = new AudioInputStream(bis, mp3Format, pcm44110.length / mp3Format.getFrameSize());
			AudioInputStream audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat, ais);

			ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
			int bytesRead;
			byte[] PCM = new byte[2];
			while ((bytesRead = audioIS.read(PCM)) != -1) {
				decodedPCM.write(PCM, 0, bytesRead);
			}

			pcmData = byteToShort(decodedPCM.toByteArray());
			pcm44110 = null;
			// In case of implosion I put you to trash.
			ais.close();
			audioIS.close();
		} catch (JavaLayerException e) {
			throw new InvalidFileFormatException();
		}
		return pcmData;
	}

	@Override
	public boolean loadMusic(String name, short[] PCMdata) {
		boolean existingAlias = loadedMusic.keySet().contains(name);
		if (!existingAlias) {
			loadedMusic.put(name, PCMdata);
		}
		return !existingAlias;
	}

	@Override
	public boolean unloadMusic(String name) {
		boolean existingAlias = loadedMusic.keySet().contains(name);
		if (existingAlias) {
			loadedMusic.remove(name);
		}
		return existingAlias;
	}

	public Set<String> getAlias() {
		return loadedMusic.keySet();
	}

	public short[] getPCMDATA(String alias) {
		return loadedMusic.get(alias);
	}
}
