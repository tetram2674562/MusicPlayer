package net.tetram26.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.tetram26.plugin.MusicPlayerPlugin;

public class ConnectionListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
	for (String thread : MusicPlayerPlugin.getInstance().activeMusicThread.keySet()) {
	    MusicPlayerPlugin.getInstance().activeMusicThread.get(thread).addPlayer(event.getPlayer().getName());
	    
	    /*
	     * List<String> players = (List<String>)
	     * MusicPlayerPlugin.getInstance().broadcastPlayers.get(thread).get(2); if
	     * (!players.contains(event.getPlayer().getName())) {
	     * players.add(event.getPlayer().getName()); Set<VoicePlayer> playersSet =
	     * (Set<VoicePlayer>)
	     * MusicPlayerPlugin.getInstance().broadcastPlayers.get(thread).get(1);
	     * playersSet.add(MusicPlayerPlugin.getInstance().getAddon().getVoiceServer().
	     * getPlayerManager() .getPlayerByName(event.getPlayer().getName())
	     * .orElseThrow(() -> new IllegalStateException("Player not found")));
	     * 
	     * ServerBroadcastSource source = (ServerBroadcastSource)
	     * MusicPlayerPlugin.getInstance().broadcastPlayers.get(0);
	     * source.setPlayers(playersSet);
	     * MusicPlayerPlugin.getInstance().broadcastPlayers.get(thread).set(1,
	     * playersSet);
	     * MusicPlayerPlugin.getInstance().broadcastPlayers.get(thread).set(2, players);
	     * 
	     * }
	     */
	}

    }
}
