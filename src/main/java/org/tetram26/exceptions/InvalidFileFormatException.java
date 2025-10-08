// Copyright (c) 2024-2025 tetram2674562
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
package org.tetram26.exceptions;

import org.tetram26.api.IInvalidFileFormatException;

import java.io.Serial;

public class InvalidFileFormatException extends Exception implements IInvalidFileFormatException {

	@Serial
    private static final long serialVersionUID = 1L;

}
