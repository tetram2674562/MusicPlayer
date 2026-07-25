package org.tetram26.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.tetram26.controller.Controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SpeakerManager {
    private final Controller controller;
    private final File locationFile;
    private final YamlConfiguration speakerYamlConfiguration;
    private final File speakerFile;
    // List of type of speakers
    private final List<SpeakerType> speakers = new ArrayList<>();
    private List<SpeakerLocation> locations = Collections.synchronizedList(new ArrayList<>());

    public SpeakerManager(Controller controller, File locationFile, File speakerFile) {
        this.controller = controller;
        this.locationFile = locationFile;
        this.speakerYamlConfiguration = YamlConfiguration.loadConfiguration(speakerFile);
        this.speakerFile = speakerFile;
        if (!speakerFile.exists()) {
            try {
                speakerFile.createNewFile();
                saveDefaultConfig();
            } catch (IOException ignore) {
            }
        }
    }

    public void loadTypeFromFile() {
        synchronized (speakerYamlConfiguration) {
            // unload speakers
            unloadSpeakers();
            // reload speakers types
            ConfigurationSection section = speakerYamlConfiguration.getConfigurationSection("speakers");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    ConfigurationSection speakerSection = section.getConfigurationSection(key);

                    if (speakerSection == null) continue;

                    String song = speakerSection.getString("song", "");
                    int range = speakerSection.getInt("range", 16);
                    SpeakerType type = new SpeakerType(controller, key, song, range);
                    locations.stream().filter(location -> location.speakerType().equals(key)).forEach(location -> type.createNewInstance(location.location()));
                    speakers.add(type);
                    System.out.println(key);
                }
            }
        }
    }

    public void saveDefaultConfig() throws IOException {
        synchronized (speakerFile) {
            YamlConfiguration config = new YamlConfiguration();
            ConfigurationSection section = config.createSection("speakers");
            ConfigurationSection speakerSection = section.createSection("SPEAKER-ID");
            speakerSection.set("song", "potato_strikes_back");
            speakerSection.set("range", 16);
            config.save(speakerFile);
        }
    }


    public void loadLocationsFromFile() throws IOException {
        synchronized (locationFile) {

            if (!locationFile.exists())
                locationFile.createNewFile();


            try (FileReader reader = new FileReader(locationFile)) {
                YamlConfiguration locations = YamlConfiguration.loadConfiguration(reader);
                ConfigurationSection section = locations.getConfigurationSection("locations");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        ConfigurationSection speakerLoc = section.getConfigurationSection(key);
                        if (speakerLoc == null)
                            continue;

                        this.locations.add(new SpeakerLocation(speakerLoc.getLocation("location"), speakerLoc.getString("id")));
                    }
                }
            }

        }
    }

    public void saveLocationToFile() throws IOException {
        synchronized (locationFile) {

            if (!locationFile.exists())
                locationFile.createNewFile();

            YamlConfiguration locations = new YamlConfiguration();
            ConfigurationSection section = locations.createSection("locations");
            int i = 0;
            for (SpeakerLocation location : this.locations) {
                ConfigurationSection locationSection = section.createSection(String.valueOf(i));
                locationSection.set("location", location.location());
                locationSection.set("id", location.speakerType());
                ++i;
            }
            locations.save(locationFile);
        }
    }

    public void unloadSpeakers() {
        speakers.forEach(SpeakerType::unload);
        speakers.clear();
    }

    public void addSpeaker(String s, @NotNull Location location) {
        speakers.stream().filter(type -> type.getId().equals(s)).forEach(type -> {
            type.createNewInstance(location);
            locations.add(new SpeakerLocation(location, s));
        });
        try {
            saveLocationToFile();
        } catch (IOException ignored) {
        }
    }

    public void removeSpeaker(Location location) {
        speakers.forEach(type -> {
            type.removeInstance(location);
            locations.removeIf(val -> val.location().equals(location));
        });
        try {
            saveLocationToFile();
        } catch (IOException ignored) {
        }
    }

    public @Nullable List<String> getSpeakerTypes() {
        return speakers.stream().map(SpeakerType::getId).toList();
    }

    public ItemStack getItem(@NonNull @NotNull String speakerId) {
        AtomicReference<ItemStack> result = new AtomicReference<>(ItemStack.of(Material.AIR));
        speakers.stream().filter(speaker -> speaker.getId().equals(speakerId)).findFirst().ifPresent(speakerType -> result.set(speakerType.getItem()));

        return result.get();

    }

}
