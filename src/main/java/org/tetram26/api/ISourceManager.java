// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.api;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.tetram26.addon.MusicAddon;

import su.plo.voice.api.server.audio.line.ServerSourceLine;
import su.plo.voice.api.server.audio.source.ServerBroadcastSource;
import su.plo.voice.api.server.audio.source.ServerDirectSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.audio.source.ServerStaticSource;
import su.plo.voice.api.server.player.VoicePlayer;

public interface ISourceManager {
	/**
	 * Create a broadcast source
	 *
	 * @param sourceLine The source line to play sound on
	 * @param playerList A list of player
	 * @return The newly created broadcast source
	 */
	public ServerBroadcastSource createBroadcastSource(ServerSourceLine sourceLine, Set<VoicePlayer> playerList,
			String thread);

	/**
	 * Create a direct source
	 *
	 * @param sourceLine The source line to play sound on
	 * @param username   The username of the player
	 * @return The newly created direct source
	 */
	public ServerDirectSource createDirectSource(ServerSourceLine sourceLine, String username);

	/**
	 * Create a direct source
	 *
	 * @param sourceLine The source line to play sound on
	 * @param username   The username of the player
	 * @return The newly created direct source
	 */
	public ServerPlayerSource createPlayerSource(ServerSourceLine sourceLine, String username);

	/**
	 * Createa set of voiceplayer for a given list of player
	 *
	 * @param playerList the list of player
	 * @return The set of all voiceplayer
	 */
	public Set<VoicePlayer> createPlayerVoiceSet(List<String> playerList);

	/**
	 * Create a source line
	 *
	 * @param name  The name of the source line
	 * @param addon The addon
	 * @return The newly created source line
	 */
	public ServerSourceLine createSourceLine(String name, MusicAddon addon);

	ServerStaticSource createBlockSource(ServerSourceLine sourceLine, Location location);
}
