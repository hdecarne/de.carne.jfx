/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.jfx.stage.logview;

import java.util.logging.Level;

import de.carne.jfx.scene.image.ImageRegistry;

/**
 * Utility class used to define the images used to display log messages in the
 * UI.
 */
public class LogViewImages {

	private LogViewImages() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Log level images.
	 */
	public static final ImageRegistry<Level> LEVEL_IMAGES = new ImageRegistry<>(
			(o1, o2) -> Integer.compare(o1.intValue(), o2.intValue()));

}
