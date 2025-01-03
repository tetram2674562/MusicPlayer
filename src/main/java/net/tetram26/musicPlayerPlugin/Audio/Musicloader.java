package net.tetram26.musicPlayerPlugin.Audio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

public class Musicloader {
	
	
	public static short[] readPCMfromFile(String path) throws IOException {
		short[] pcmData;
		// Read ALL bytes from the file
		File pcmFile = new File(path);
		byte[] pcmBytes = Files.readAllBytes(pcmFile.toPath());
		
		// Convert byte[] to short[]
		pcmData = byteToShort(pcmBytes);
		return pcmData;

	}
	
	public static short[] byteToShort(byte[] byteData) {
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian
		short[] shortData = new short[byteData.length / 2];
		for (int i = 0; i < shortData.length; i++) {
		    shortData[i] = byteBuffer.getShort();
		}
		return shortData;
	}
}
