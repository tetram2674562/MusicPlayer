// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.api;

import su.plo.voice.api.server.audio.line.ServerSourceLine;

public interface IMusicPlayerAPI {
	/**
	 * Allow you to get the sound controller USE IT AT YOUR OWN RISK.
	 *
	 * @return The sound controller for the Music Player API
	 */
	public IController getController();

	/**
	 * Get the source line from music player
	 *
	 * @return The music player dedicated server source line.
	 */
	public ServerSourceLine getMusicPlayerSourceLine();

}
