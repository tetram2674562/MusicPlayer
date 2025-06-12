package net.tetram26.languageHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import net.tetram26.plugin.MusicPlayerPlugin;

public class LanguageHandler {
// REWORK THIS FCKING SHIT 
	private InputStream languageIS;
	private File languageFile;

	public LanguageHandler(InputStream IS) {
		languageFile = writeLanguageFile(IS);

	}

	public File getLanguageFile() {
		return languageFile;
	}

	private File writeLanguageFile(InputStream file) {
		try {
			byte[] data = file.readAllBytes();
			File languageFile = new File(
					Paths.get(MusicPlayerPlugin.getInstance().getDataPath().toString(), "language.toml").toString());
			OutputStream outFile = new FileOutputStream(languageFile);
			outFile.write(data);
			outFile.close();
		} catch (IOException e) {
			System.out.println("How did we got there?");
		}
		return languageFile;
	}
}
