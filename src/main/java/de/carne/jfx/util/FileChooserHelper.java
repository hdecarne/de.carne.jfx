/*
 * Copyright (c) 2014-2016 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.jfx.util;

import java.util.Arrays;

import de.carne.util.Strings;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Utility class providing {@link FileChooser} related functions.
 */
public final class FileChooserHelper {

	private FileChooserHelper() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Create an {@link ExtensionFilter} object from a {@link String}.
	 * <p>
	 * The submitted string has to contain at least two tokens separated by the
	 * '|' symbol. The first token is used as the filter's description. All
	 * following tokens are used as filter extensions (e.g. "Binary
	 * data|*.bin|*.dat").
	 *
	 * @param str The {@link String} to use for filter creation.
	 * @return The created {@link ExtensionFilter} object.
	 */
	public static ExtensionFilter filterFromString(String str) {
		String[] tokens = Strings.split(str, "|");

		assert tokens.length >= 2;

		return new ExtensionFilter(tokens[0], Arrays.copyOfRange(tokens, 1, tokens.length));
	}

}
