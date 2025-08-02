package org.tetram26.listener;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.tetram26.audio.MusicSender;
import org.tetram26.plugin.MusicPlayerPlugin;

public class JukeboxListener implements Listener {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJukeboxInteracted(PlayerInteractEvent event) {
		// Si le block avec lequel le joueur a intéragie est un jukebox
		if (event.getClickedBlock().getType().equals(Material.JUKEBOX) && event.getAction().isRightClick()) {
			// Alors on vérifie ce qu'il y a dans le jukebox
			Jukebox data = (Jukebox)event.getClickedBlock().getState();
			Location musicLocation = event.getClickedBlock().getLocation().add(0.5d,1.5d,0.5d);
			// Si le jukebox a un disc
			if (data.hasRecord()) {
				// On tente de trouver un sender qui est à cette position
				Optional<MusicSender> sender = MusicPlayerPlugin.getInstance().getController().checkForMusicThreadAtLocation(
						musicLocation);
				// S'il y en a un : 
				if (sender.isPresent()) {
					// On l'arrête
					sender.get().stop();
				} 
			}
			// Sinon
			else {
				// On check si l'item disc 5 dispose d'un lore spécial
				if (event.getItem() != null && event.getItem().getType().equals(Material.MUSIC_DISC_5) && checkMusicName(event.getItem()) != "") {
					// Il en a un !!!

					String musicName = checkMusicName(event.getItem());
					
					if (MusicPlayerPlugin.getInstance().getController().getMusicLoader().getAlias().contains(musicName)) {
						String uuid = UUID.randomUUID().toString();
						// Donc on joue la music !
						MusicPlayerPlugin.getInstance().getController().playAudioAt(musicLocation, 
																					MusicPlayerPlugin.getInstance().getController().getMusicLoader().getPCMDATA(musicName), 
																					MusicPlayerPlugin.getInstance().getMusicPlayerSourceLine(),
																					musicName + "jukebox_"+uuid, 
											
																					MusicPlayerPlugin.getInstance().getConfig().getInt("jukebox-distance"));
						// Cancel default interaction — this prevents vanilla disc placement + sound
						event.setCancelled(true);

						// Manually place the record WITHOUT triggering vanilla sound
						event.getClickedBlock().setType(Material.JUKEBOX); // Just to be safe
						data.setRecord(event.getItem().clone()); // Visual feedback
						data.update(true); // Apply state
						data.stopPlaying();
						data.update(true);
						// Remove disc from player's hand
						event.getPlayer().getInventory().setItemInMainHand(null);
					}
					}
			
			}
		}
	}
	private String checkMusicName(ItemStack item) {
		return item.getPersistentDataContainer().getOrDefault(new NamespacedKey(MusicPlayerPlugin.getInstance(),"music"), PersistentDataType.STRING, "");
	}
}
