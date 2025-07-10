// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.api;

import java.io.IOException;
import java.nio.file.Path;

public interface IStartupLoader {

	/**
	 * Try to find a correct path for a given JSON filename, if the file doesn't
	 * exist : create it.
	 *
	 * @param name The filename
	 * @return The path of the file
	 * @throws IOException if the file doesn't exist (It should not throw)
	 */
	public Path getStartupJSONPath(String name) throws IOException;

	/**
	 * Load Audio file from a given JSON file
	 *
	 * @param JSONpath the path of the JSON file
	 */
	public void loadPCMfromJSON(String JSONpath);
}
