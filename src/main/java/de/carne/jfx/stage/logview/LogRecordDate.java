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
package de.carne.jfx.stage.logview;

import java.util.Date;

/**
 * Wrapper class for {@link Date} used to apply a log specific date formating.
 */
public class LogRecordDate extends Date {

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = 5846084302347518381L;

	/**
	 * Construct {@code LogRecordDate}.
	 *
	 * @param date The initial date value in ms.
	 * @see Date#Date(long)
	 */
	public LogRecordDate(long date) {
		super(date);
	}

	@Override
	public String toString() {
		return LogViewFormats.TIME_FORMAT.format(this);
	}

}
