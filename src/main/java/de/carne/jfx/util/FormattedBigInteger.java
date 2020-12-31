/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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

import java.math.BigInteger;
import java.text.Format;

/**
 * Simple wrapper class for {@link BigInteger} to provide a custom formatted version for display in JavaFX UI elements
 * which use the {@link #toString()} method for date formatting.
 */
public class FormattedBigInteger extends BigInteger {

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = -6822461794813461310L;

	/**
	 * The format.
	 */
	public final Format format;

	/**
	 * Construct {@code FormattedDate}.
	 *
	 * @param format The {@link Format} to use for number display.
	 * @param value The number value bytes.
	 * @see BigInteger#BigInteger(byte[])
	 */
	public FormattedBigInteger(Format format, byte[] value) {
		super(value);
		this.format = format;
	}

	/**
	 * Create a formatted number from another number.
	 * <p>
	 * If the submitted number is already of type {@code FormattedBigInteger} the instance is returned directly
	 * otherwise the submitted number is wrapped using the submitted format.
	 * </p>
	 *
	 * @param format The {@link Format} to use for number display.
	 * @param number The number value to convert.
	 * @return The created formatted number.
	 */
	public static FormattedBigInteger fromBigInteger(Format format, BigInteger number) {
		return (number instanceof FormattedBigInteger ? (FormattedBigInteger) number
				: new FormattedBigInteger(format, number.toByteArray()));
	}

	@Override
	public String toString() {
		return this.format.format(this);
	}

}
