package net.tetram26.audio;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.UnsupportedAudioFileException;

public interface IMusicLoader {
    
    /**
     * Load PCM data from a WAV file
     * @param path The path of the WAV file
     * @return A short array containing the PCM data
     * @throws UnsupportedAudioFileException If It isn't a WAV file
     * @throws IOException If there isn't any WAV file at the given location.
     */
    public short[] loadPCMfromWAV(String path) throws UnsupportedAudioFileException, IOException;
    
    /**
     * Load PCM data from a PCM file
     * @param path The path of the PCM file
     * @return A short array containing the PCM data
     * @throws IOException If there isn't any PCM file at the given location.
     */
    public short[] loadPCMfromFile(String path) throws IOException;
    
    /**
     * Load PCM data from a distant WAV file 
     * @param link The direct URL to the file
     * @return A short array containing the PCM data of the file
     * @throws UnsupportedAudioFileException If the distant file isn't a WAV file
     * @throws IOException If there are no such file at the give URL
     * @throws URISyntaxException If an invalid URL is provided
     */
    public short[] loadPCMfromURL(String link) throws UnsupportedAudioFileException, IOException, URISyntaxException;
    
    /**
     * Convert a byte array into a short array 
     * @param byteData A byte array
     * @return A short array 
     */
    public short[] byteToShort(byte[] byteData);
}
