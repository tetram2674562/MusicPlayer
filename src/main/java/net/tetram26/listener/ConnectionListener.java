package net.tetram26.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.tetram26.audio.MusicSender;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ConnectionListener implements Listener{
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLeave(PlayerQuitEvent event) {
		// For each thread registered
		for (String thread : MusicPlayerPlugin.playerThread.keySet()) {
			// if the active thread is owned by the player...
			
			if (MusicPlayerPlugin.playerThread.get(thread).equals(event.getPlayer().getName())) {
				// cleanup
				System.out.println("cleaning.");
				MusicPlayerPlugin.playerThread.remove(thread);
				// Let's get that music sender
				MusicSender currentMusicPlayer = MusicPlayerPlugin.activeMusicThread.get(thread);
				// We close the music sender and remove it.
				currentMusicPlayer.stop();
				MusicPlayerPlugin.activeMusicThread.remove(thread);
			}
		}
	}
}
