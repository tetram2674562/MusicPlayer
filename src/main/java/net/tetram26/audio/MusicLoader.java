package net.tetram26.audio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MusicLoader implements IMusicLoader {
	private final static AudioFormat plasmoVoiceFormat = new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
	
	
	public short[] loadPCMfromWAV(String path) throws UnsupportedAudioFileException,IOException {
		short[] pcmData = null;
		File audioFile = new File(path);
		
		
		AudioInputStream audioIS = AudioSystem.getAudioInputStream(audioFile);
		audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat,audioIS);
		ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
		int bytesRead;
		byte[] PCM = new byte[2];
		while ((bytesRead = audioIS.read(PCM)) != -1) {
		    decodedPCM.write(PCM, 0, bytesRead);
		}
			
		pcmData = byteToShort(decodedPCM.toByteArray());
		
		return pcmData;
	}
	
	public short[] loadPCMfromFile(String path) throws IOException {
		short[] pcmData;
		// Read ALL bytes from the file
		File pcmFile = new File(path);
		byte[] pcmBytes = Files.readAllBytes(pcmFile.toPath());
		
		// Convert byte[] to short[]
		pcmData = byteToShort(pcmBytes);
		return pcmData;

	}
	
	public short[] loadPCMfromURL(String link) throws UnsupportedAudioFileException, IOException, URISyntaxException {
		short[] pcmData = null;
		URI uri = new URI(link);
		AudioInputStream audioIS = AudioSystem.getAudioInputStream(uri.toURL());
		audioIS = AudioSystem.getAudioInputStream(plasmoVoiceFormat,audioIS);
		ByteArrayOutputStream decodedPCM = new ByteArrayOutputStream();
		int bytesRead;
		byte[] PCM = new byte[2];
		while ((bytesRead = audioIS.read(PCM)) != -1) {
		    decodedPCM.write(PCM, 0, bytesRead);
		}
		
		
		pcmData = byteToShort(decodedPCM.toByteArray());
		
		return pcmData;
	}
	
	public short[] byteToShort(byte[] byteData) {
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian
		short[] shortData = new short[byteData.length / 2];
		for (int i = 0; i < shortData.length; i++) {
		    shortData[i] = byteBuffer.getShort();
		}
		return shortData;
	}
}
