/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple wrapper class for {@link Date} to provide a short formatted version
 * for display in JavaFX UI elements which use the {@link #toString()} method
 * for date formatting.
 */
public class ShortDate extends Date {

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = -5621027739579780082L;

	/**
	 * The short date formatter.
	 */
	public static final DateFormat FORMAT = new SimpleDateFormat();

	/**
	 * Construct {@code ShortDate}.
	 *
	 * @see Date#Date()
	 */
	public ShortDate() {
		super();
	}

	/**
	 * Construct {@code ShortDate}.
	 *
	 * @param date The initial date value in ms.
	 * @see Date#Date(long)
	 */
	public ShortDate(long date) {
		super(date);
	}

	@Override
	public String toString() {
		return FORMAT.format(this);
	}

}
