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
package de.carne.jfx.scene.control;

import java.util.Collection;
import java.util.logging.LogRecord;

import de.carne.boot.logging.Log;
import de.carne.boot.logging.LogLevel;
import de.carne.check.Nullable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Utility class providing {@link Alert} related functions.
 */
public final class Alerts {

	private Alerts() {
		// Make sure this class is not instantiated from outside
	}

	private static Log LOG = new Log();

	/**
	 * Create an unexpected error {@link Alert} of type {@link AlertType#ERROR}.
	 *
	 * @param throwable The optional {@link Throwable} providing the alert details.
	 * @return The created alert.
	 */
	public static Alert unexpected(Throwable throwable) {
		return error(AlertType.ERROR, AlertsI18N.formatSTR_MESSAGE_UNEXPECTED_ERROR(), throwable);
	}

	/**
	 * Create an application error message {@link Alert} with {@link Throwable} details.
	 *
	 * @param type The alert type to create.
	 * @param message The error message to display.
	 * @param throwable The optional {@link Throwable} providing the alert details.
	 * @return The created alert.
	 */
	public static Alert error(AlertType type, String message, Throwable throwable) {
		logAlertMessage(type, message, throwable);

		Alert alert = new Alert(type, message, ButtonType.OK);

		alert.setHeaderText(AlertsI18N.formatSTR_MESSAGE_APPLICATION_ERROR());
		return DialogHelper.setExceptionContent(alert, throwable);
	}

	/**
	 * Create a generic application message {@link Alert}.
	 *
	 * @param type The alert type to create.
	 * @param message The message to display.
	 * @param buttons The buttons to display.
	 * @return The created alert.
	 */
	public static Alert message(AlertType type, String message, ButtonType... buttons) {
		return new Alert(type, message, buttons);
	}

	/**
	 * Create an application error message {@link Alert} with {@link LogRecord} details.
	 *
	 * @param type The alert type to create.
	 * @param message The error message to display.
	 * @param logs The optional collection of {@link LogRecord}s providing the alert details.
	 * @return The created alert.
	 */
	public static Alert logs(AlertType type, String message, Collection<LogRecord> logs) {
		logAlertMessage(type, message, null);

		Alert alert = new Alert(type, message, ButtonType.OK);

		alert.setHeaderText(AlertsI18N.formatSTR_MESSAGE_APPLICATION_ERROR());
		return DialogHelper.setLogRecordsContent(alert, logs);
	}

	private static void logAlertMessage(AlertType type, String message, @Nullable Throwable throwable) {
		if (AlertType.ERROR.equals(type)) {
			LOG.log(LogLevel.LEVEL_ERROR, throwable, message);
		} else if (AlertType.WARNING.equals(type)) {
			LOG.log(LogLevel.LEVEL_WARNING, throwable, message);
		} else if (AlertType.INFORMATION.equals(type)) {
			LOG.log(LogLevel.LEVEL_INFO, throwable, message);
		}
	}

}
