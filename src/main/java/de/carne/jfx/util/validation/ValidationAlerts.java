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
package de.carne.jfx.util.validation;

import de.carne.jfx.scene.control.DialogHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Utility class for creation of validation related {@link Alert} dialogs.
 */
public final class ValidationAlerts {

	private ValidationAlerts() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Creates an {@link Alert} of type {@link AlertType#ERROR} and set it up using the submitted
	 * {@link ValidationException}.
	 *
	 * @param e The {@link ValidationException} providing the alert details.
	 * @return The created alert.
	 */
	public static Alert error(ValidationException e) {
		Alert alert = new Alert(AlertType.ERROR, e.getLocalizedMessage(), ButtonType.OK);

		alert.setHeaderText(ValidationAlertsI18N.formatSTR_MESSAGE_VALIDATION_ERROR());
		DialogHelper.setExceptionContent(alert, e.getCause());
		return alert;
	}

}
