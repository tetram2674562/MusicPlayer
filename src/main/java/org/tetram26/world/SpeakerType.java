package org.tetram26.world;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.tetram26.controller.Controller;
import org.tetram26.plugin.MusicPlayerPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SpeakerType {
    @Getter
    private final String id;
    private final String song;
    private final int range;
    private final Controller controller;
    private final List<Speaker> instances = Collections.synchronizedList(new ArrayList<>());

    public SpeakerType(Controller controller, String id,String trackName, int range) {
        this.id = id;
        this.song = trackName;
        this.range = range;
        this.controller = controller;
    }


    public void createNewInstance(Location position) {
        System.out.println(position);
        instances.add(new Speaker(controller.playAudioAt(
                position, song, MusicPlayerPlugin.getInstance().getAddon().getMusicSourceLine(), range), position, controller));
    }

    public void unload() {
        instances.forEach(Speaker::unload);
        instances.clear();
    }

    public ItemStack getItem() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(java.util.UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTczODE0NTYwMzA2NywKICAicHJvZmlsZUlkIiA6ICJiZmQ3MjMxMGNmYWY0Yjc5OTNlYzhiYzU3ODg3YzU5ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbHBoYVNwQW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY1NmM0MTFhMmZkNTViZmMwNTRlNjI4YzEyNmU0Nzk2YmU2NTg2YWRhYmQ0YWYyNGRkYzcwYTM3ZTQ3YWUzYSIKICAgIH0KICB9Cn0="));

        meta.setPlayerProfile(profile);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        MusicPlayerPlugin plugin = MusicPlayerPlugin.getInstance();
        NamespacedKey key = NamespacedKey.fromString("speaker-type", plugin);
        if (key != null)
            container.set(key, PersistentDataType.STRING, id);
        head.setItemMeta(meta);
        return head;
    }

    public void removeInstance(Location location) {
        instances.stream().filter(speaker -> speaker.getLoc().equals(location)).forEach(Speaker::unload);
        instances.removeIf(speaker -> speaker.getLoc().equals(location));
    }
}
