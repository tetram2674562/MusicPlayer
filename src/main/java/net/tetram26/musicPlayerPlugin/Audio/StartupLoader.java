package net.tetram26.musicPlayerPlugin.Audio;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.tetram26.musicPlayerPlugin.MusicPlayerPlugin;

public class StartupLoader {
	// Chargement des fichiers PCM comme indiqués dans le JSON.
	@SuppressWarnings("unchecked")
	public static void loadPCMfromJSON(String JSONpath) {

		JSONParser jsonParser = new JSONParser();

		try {

			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(JSONpath.toString()));
			jsonObject.forEach((name,path) -> {
				try {
					String filepath = Paths.get(MusicPlayerPlugin.musicPath.toString(),(String) path).toString();
					MusicPlayerPlugin.loadedMusic.put((String) name,MusicPlayerPlugin.getAddon().loadAudio(filepath));
					System.out.println("File :'"+name+"' loaded");
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

	}}
	public static Path getStartupJSONPath(String name) throws IOException {
		Path startup = Paths.get(MusicPlayerPlugin.configPath.toString(),name);
		// Si le fichier n'exite pas encore on le crée !
		if (!startup.toFile().exists()) {
		startup.toFile().createNewFile();
		FileWriter startupJson = new FileWriter(startup.toFile());
		startupJson.append("{\n}");
		startupJson.close();

		}
		return startup;
	}
}
