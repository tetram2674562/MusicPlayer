package org.tetram26.audio;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("MusicLoader Tests")
public class MusicLoaderTest {

	private MusicLoader loader;

	@BeforeEach
	void setUp() {
		loader = new MusicLoader();
	}

	@Test
	@DisplayName("Should convert bytes to shorts correctly (little-endian)")
	void testByteToShort() {
		// Create test data: 0x0001 and 0x0002 in little-endian
		byte[] byteData = new byte[] { 0x01, 0x00, 0x02, 0x00 };

		short[] result = loader.byteToShort(byteData);

		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals(1, result[0]); 
		assertEquals(2, result[1]); 
	}

	@Test
	@DisplayName("Should handle empty byte array")
	void testByteToShortEmpty() {
		byte[] emptyData = new byte[] {};
		short[] result = loader.byteToShort(emptyData);

		assertNotNull(result);
		assertEquals(0, result.length);
	}

	@Test
	@DisplayName("Should convert negative values correctly")
	void testByteToShortNegative() {
		byte[] byteData = new byte[] { (byte) 0xFF, (byte) 0xFF };

		short[] result = loader.byteToShort(byteData);

		assertEquals(1, result.length);
		assertEquals(-1, result[0]);
	}

	@Test
	@DisplayName("Should handle large values")
	void testByteToShortLargeValues() {
		byte[] byteData = new byte[] { (byte) 0xFF, 0x7F };

		short[] result = loader.byteToShort(byteData);

		assertEquals(1, result.length);
		assertEquals(Short.MAX_VALUE, result[0]);
	}

	@Test
	@DisplayName("Should get empty alias list on startup")
	void testGetAliasEmpty() {
		var aliases = loader.getAlias();

		assertNotNull(aliases);
		assertTrue(aliases.isEmpty());
	}

	@Test
	@DisplayName("Should unload non-existent music return false")
	void testUnloadMusicNonExistent() {
		boolean result = loader.unloadMusic("nonexistent");

		assertFalse(result);
	}

	@Test
	@DisplayName("Should load PCM data and retrieve it")
	void testLoadAndRetrieveMusic() {
		String musicName = "test-music";
		short[] testData = new short[] { 100, 200, 300 };

		boolean loaded = loader.loadMusic(musicName, testData);

		assertTrue(loaded);
		var aliases = loader.getAlias();
		assertTrue(aliases.contains(musicName));
	}

	@Test
	@DisplayName("Should not load duplicate music names")
	void testLoadMusicDuplicate() {
		String musicName = "duplicate";
		short[] testData = new short[] { 1, 2, 3 };

		boolean firstLoad = loader.loadMusic(musicName, testData);
		boolean secondLoad = loader.loadMusic(musicName, testData);

		assertTrue(firstLoad);
		assertFalse(secondLoad); // Should return false because it already exists
	}

	@Test
	@DisplayName("Should retrieve correct PCM data supplier")
	void testGetPCMDATA() {
		String musicName = "sample";
		short[] originalData = new short[] { 42, 84, 126 };

		loader.loadMusic(musicName, originalData);
		var supplier = loader.getPCMDATA(musicName);

		assertNotNull(supplier);
		short[] retrievedData = supplier.get();
		assertArrayEquals(originalData, retrievedData);
	}

	@Test
	@DisplayName("Should unload existing music")
	void testUnloadMusicExisting() {
		String musicName = "to-remove";
		short[] testData = new short[] { 1, 2, 3 };

		loader.loadMusic(musicName, testData);
		boolean unloaded = loader.unloadMusic(musicName);

		assertTrue(unloaded);
		assertFalse(loader.getAlias().contains(musicName));
	}
}
