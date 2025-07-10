// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.startup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.tetram26.api.IStartupLoader;
import org.tetram26.audio.MusicLoader;
import org.tetram26.plugin.MusicPlayerPlugin;

public class StartupLoader implements IStartupLoader {
	@Override
	public Path getStartupJSONPath(String name) throws IOException {
		Path startup = Paths.get(MusicPlayerPlugin.getInstance().getConfigPath().toString(), name);
		// Si le fichier n'exite pas encore on le crée !
		if (!startup.toFile().exists()) {
			startup.toFile().createNewFile();
			FileWriter startupJson = new FileWriter(startup.toFile());
			startupJson.append("{\n}");
			startupJson.close();
		}
		return startup;
	}

	// Chargement des fichiers PCM comme indiqués dans le JSON.
	@Override
	@SuppressWarnings("unchecked")
	public void loadPCMfromJSON(String JSONpath) {
		MusicLoader loader = MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader();
		JSONParser jsonParser = new JSONParser();

		try {

			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(JSONpath.toString()));
			jsonObject.forEach((name, path) -> {
				try {
					String filename = (String) path;
					if (!filename.contains("{") && !filename.contains("}")) {
						String filepath = Paths.get(MusicPlayerPlugin.getInstance().getMusicPath().toString(), filename)
								.toString();

						String extension = "";

						int i = filename.lastIndexOf('.');
						if (i > 0) {
							extension = filename.substring(i + 1);
						}
						if (extension.equals("pcm")) {
							MusicPlayerPlugin.getInstance().getComponentLogger()
									.error("PCM Loading isn't supported anymore, please use WAV loading instead !");
							// MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader()
							// .loadMusic((String) name, loader.loadPCMfromFile(filepath.toString()));
						} else if (extension.equals("mp3")) {
							// MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader()
							// .loadMusic((String) name, loader.loadPCMfromMP3(filepath.toString()));
						} else {
							MusicPlayerPlugin.getInstance().getAddon().getController().getMusicLoader()
									.loadMusic((String) name, loader.loadPCMfromWAV(filepath.toString()));
						}
						MusicPlayerPlugin.getInstance().getComponentLogger().info("File :'" + name + "' loaded");
					}
				} catch (IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
