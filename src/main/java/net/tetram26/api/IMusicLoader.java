package net.tetram26.api;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import net.tetram26.exceptions.InvalidFileFormatException;

public interface IMusicLoader {

	/**
	 * Load PCM data from a WAV file
	 *
	 * @param path The path of the WAV file
	 * @return A short array containing the PCM data
	 * @throws UnsupportedAudioFileException If It isn't a WAV file
	 * @throws IOException                   If there isn't any WAV file at the
	 *                                       given location.
	 */
	public short[] loadPCMfromWAV(String path) throws UnsupportedAudioFileException, IOException;

	/**
	 * Load PCM data from a PCM file
	 *
	 * @param path The path of the PCM file
	 * @return A short array containing the PCM data
	 * @throws IOException If there isn't any PCM file at the given location.
	 */
	public short[] loadPCMfromFile(String path) throws IOException;

	/**
	 * Load PCM data from a distant WAV file
	 *
	 * @param link The direct URL to the file
	 * @return A short array containing the PCM data of the file
	 * @throws UnsupportedAudioFileException If the distant file isn't a WAV file
	 * @throws IOException                   If there are no such file at the give
	 *                                       URL
	 * @throws URISyntaxException            If an invalid URL is provided
	 * @throws InvalidFileFormatException 
	 */
	public short[] loadPCMfromURL(String link) throws InvalidFileFormatException, IOException, URISyntaxException, InvalidFileFormatException;

	/**
	 * Convert a byte array into a short array
	 *
	 * @param byteData A byte array
	 * @return A short array
	 */
	public short[] byteToShort(byte[] byteData);

	/**
	 * Load PCM data from an mp3 file
	 * 
	 * @param path The file path
	 * @return A short array containing the PCM data of the file
	 * @throws IOException        If the file doesn't exist.
	 * @throws JavaLayerException
	 */
	public short[] loadPCMfromMP3(String path) throws IOException, InvalidFileFormatException;

	/**
	 * Store PCM Data inside the memory.
	 * 
	 * @param name    The alias for those pcm data
	 * @param PCMdata The pcmdata in a short array
	 * @return true if it was successfully stored inside the memory, false if not.
	 */
	public boolean loadMusic(String name, short[] PCMdata);

	/**
	 * Remove the PCM Data inside the memory at the given alias.
	 * 
	 * @param name The alias for those pcm data
	 * @return true if it was successfully remove from the memory, false if no music
	 *         like that were found.
	 */
	public boolean unloadMusic(String name);
}
