// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package net.tetram26.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.tetram26.audio.MusicSender;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ConnectionListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event) {
		for (String thread : MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName()) {
			MusicPlayerPlugin.getInstance().getAddon().getController().getThread(thread)
					.addPlayer(event.getPlayer().getName());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onLeave(PlayerQuitEvent e) {
		for (String thread : MusicPlayerPlugin.getInstance().getAddon().getController().getThreadsName()) {
			MusicSender musicThread = MusicPlayerPlugin.getInstance().getAddon().getController().getThread(thread);
			if (musicThread.hasPlayer(e.getPlayer().getName()) && !musicThread.isBroadcast()) {
				musicThread.stop();
				MusicPlayerPlugin.getInstance().getAddon().getController().removeThread(thread);
			}

		}
	}
}
