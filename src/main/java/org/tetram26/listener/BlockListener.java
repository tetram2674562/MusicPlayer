package org.tetram26.listener;

import io.papermc.paper.persistence.PersistentDataContainerView;
import io.papermc.paper.persistence.PersistentDataViewHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.tetram26.plugin.MusicPlayerPlugin;
import org.tetram26.world.SpeakerManager;

public class BlockListener implements Listener {

    private SpeakerManager manager;
    private MusicPlayerPlugin plugin;
    public BlockListener(@NotNull SpeakerManager manager, MusicPlayerPlugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        PersistentDataContainerView persistentDataView = itemStack.getPersistentDataContainer();
        NamespacedKey key = NamespacedKey.fromString("speaker-type", plugin);
        if (key != null && persistentDataView.has(key)) {
            manager.addSpeaker(persistentDataView.get(key, PersistentDataType.STRING), event.getBlock().getLocation().add(0.5,0.5,0.5));
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockPlaceEvent event) {

        manager.removeSpeaker(event.getBlock().getLocation());
    }


}
