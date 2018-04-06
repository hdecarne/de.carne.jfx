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
package de.carne.jfx.stage.logview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import de.carne.boot.check.Nullable;

/**
 * Utility class used to define the text formats used to display log messages in the UI.
 */
public final class LogViewFormats {

	private LogViewFormats() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * {@code Formatter} for log message formatting.
	 */
	public static final Formatter MESSAGE_FORMAT = new Formatter() {

		@Override
		public String format(@Nullable LogRecord record) {
			return formatMessage(record);
		}

	};

	/**
	 * {@link DateFormat} for log time formatting.
	 */
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss,SSS");

}
