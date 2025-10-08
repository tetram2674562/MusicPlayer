package org.tetram26.languageHandler;

import java.io.*;

import org.tetram26.plugin.MusicPlayerPlugin;
import su.plo.voice.api.server.resource.ResourceLoader;

public class LanguageHandler {

    public LanguageHandler(File languageFolder) throws IOException {
        ResourceLoader loader = fileName -> LanguageHandler.class.getClassLoader().getResourceAsStream(fileName); // f -> load -> new fileinputstream(f)
        MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().getLanguages().register(loader,
                languageFolder);

    }
}
