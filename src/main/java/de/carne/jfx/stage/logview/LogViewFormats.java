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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Utility class used to define the text formats used to display log messages in
 * the UI.
 */
public final class LogViewFormats {

	/**
	 * {@code Formatter} for log message formatting.
	 */
	public static final Formatter MESSAGE_FORMAT = new Formatter() {

		@Override
		public String format(LogRecord record) {
			return formatMessage(record);
		}

	};

	/**
	 * {@link DateTimeFormatter} for log time formatting.
	 */
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.S")
			.withZone(ZoneId.systemDefault());

	private LogViewFormats() {
		// Make sure this class is not instantiated from outside
	}

}
