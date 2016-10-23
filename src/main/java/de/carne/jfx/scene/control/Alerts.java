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
package de.carne.jfx.scene.control;

import de.carne.jfx.util.DialogHelper;
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

	/**
	 * Creates an unexpected error {@link Alert} of type
	 * {@link AlertType#ERROR}.
	 *
	 * @param e The optional {@link Throwable} providing the alert details.
	 * @return The created alert.
	 */
	public static Alert error(Throwable e) {
		Alert alert = new Alert(AlertType.ERROR, AlertsI18N.formatSTR_MESSAGE_UNEXPECTED_ERROR(), ButtonType.OK);

		alert.setHeaderText(AlertsI18N.formatSTR_MESSAGE_APPLICATION_ERROR());
		DialogHelper.setExceptionContent(alert, e);
		return alert;
	}

	/**
	 * Creates an error message {@link Alert} of type {@link AlertType#ERROR}.
	 * 
	 * @param message The error message to display.
	 * @param e The optional {@link Throwable} providing the alert details.
	 * @return The created alert.
	 */
	public static Alert error(String message, Throwable e) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);

		alert.setHeaderText(AlertsI18N.formatSTR_MESSAGE_APPLICATION_ERROR());
		DialogHelper.setExceptionContent(alert, e);
		return alert;
	}

}
