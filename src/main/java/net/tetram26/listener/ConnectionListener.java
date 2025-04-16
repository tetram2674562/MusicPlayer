package net.tetram26.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.tetram26.plugin.MusicPlayerPlugin;

public class ConnectionListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        for (String thread : MusicPlayerPlugin.getInstance().activeMusicThread.keySet()) {
            MusicPlayerPlugin.getInstance().activeMusicThread.get(thread).addPlayer(event.getPlayer().getName());
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeave(PlayerQuitEvent e) {
        for (String thread : MusicPlayerPlugin.getInstance().activeMusicThread.keySet()) {
            if (MusicPlayerPlugin.getInstance().activeMusicThread.get(thread).hasPlayer(e.getPlayer().getName())) {
                MusicPlayerPlugin.getInstance().activeMusicThread.get(thread).stop();
                MusicPlayerPlugin.getInstance().activeMusicThread.remove(thread);
            }

        }
    }
}
