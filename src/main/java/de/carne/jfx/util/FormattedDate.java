/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import java.text.Format;
import java.util.Date;

/**
 * Simple wrapper class for {@link Date} to provide a custom formatted version for display in JavaFX UI elements which
 * use the {@link #toString()} method for date formatting.
 */
public class FormattedDate extends Date {

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = -5621027739579780082L;

	/**
	 * The format.
	 */
	public final Format format;

	/**
	 * Construct {@code FormattedDate}.
	 *
	 * @param format The {@link Format} to use for date display.
	 * @see Date#Date()
	 */
	public FormattedDate(Format format) {
		this.format = format;
	}

	/**
	 * Construct {@code FormattedDate}.
	 *
	 * @param format The {@link Format} to use for date display.
	 * @param date The initial date value in ms.
	 * @see Date#Date(long)
	 */
	public FormattedDate(Format format, long date) {
		super(date);
		this.format = format;
	}

	/**
	 * Create a formatted date from another date.
	 * <p>
	 * If the submitted date is already of type {@code FormattedDate} the instance is returned directly otherwise the
	 * submitted date is wrapped using the submitted date format.
	 * </p>
	 *
	 * @param format The {@link Format} to use for date display.
	 * @param date The date value to convert.
	 * @return The created formatted date.
	 */
	public static FormattedDate fromDate(Format format, Date date) {
		return (date instanceof FormattedDate ? (FormattedDate) date : new FormattedDate(format, date.getTime()));
	}

	@Override
	public String toString() {
		return this.format.format(this);
	}

}
