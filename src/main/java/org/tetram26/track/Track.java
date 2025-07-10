package org.tetram26.track;

import java.util.function.Supplier;

public class Track {
	private short[] audioData;
	private String name;

	public Track(String name, short[] audioData) {
		this.audioData = audioData;
		this.name = name;
	}

	public Supplier<short[]> getAudioData() {
		return () -> audioData;
	}

	public String getName() {
		return name;
	}

}
