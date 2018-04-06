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

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import de.carne.boot.check.Nullable;
import de.carne.jfx.fxml.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Controller class for {@link Dialog} based windows.
 *
 * @param <R> The dialog's result type.
 * @see FXMLController
 */
public abstract class DialogController<R> extends FXMLController<Dialog<R>> {

	/**
	 * Load and create new dialog.
	 *
	 * @param <R> The dialog's result type.
	 * @param <C> The actual {@code DialogController} type.
	 * @param owner The dialog's owner (may be null).
	 * @param dialogFactory The factory function used to create the actual dialog object by invoking it with the
	 *        constructed controller.
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is bound to the newly created dialog.
	 * @throws IOException if an I/O error occurs during dialog loading.
	 */
	public static <R, C extends DialogController<R>> C loadDialog(Window owner, Function<C, Dialog<R>> dialogFactory,
			Class<C> controllerClass) throws IOException {

		return loadUI(owner, dialogFactory, controllerClass);
	}

	@Override
	public final Window getWindow() {
		return getUI().getDialogPane().getScene().getWindow();
	}

	@Override
	protected void setupUI(@Nullable Window owner, Dialog<R> dialog, Parent fxmlRoot) {
		if (!(fxmlRoot instanceof DialogPane)) {
			throw new IllegalArgumentException(
					"FXML root must be of type DialogPane; but is: " + fxmlRoot.getClass().getName());
		}
		dialog.setDialogPane((DialogPane) fxmlRoot);
		dialog.initStyle(getStyle());
		if (owner != null) {
			dialog.initOwner(owner);
			dialog.initModality(getModality());
		}
		setupDialog(dialog);
	}

	/**
	 * This function is called during dialog initialization to initialize the dialog's style.
	 * <p>
	 * The default style is {@link StageStyle#DECORATED}.
	 *
	 * @return The dialog style to use.
	 * @see StageStyle
	 */
	protected StageStyle getStyle() {
		return StageStyle.DECORATED;
	}

	/**
	 * This function is called during dialog initialization to initialize the dialog's modality.
	 * <p>
	 * The default modality is {@link Modality#WINDOW_MODAL}.
	 *
	 * @return The modality to use.
	 * @see Modality
	 */
	protected Modality getModality() {
		return Modality.APPLICATION_MODAL;
	}

	/**
	 * This function is called during dialog initialization to perform the actual dialog setup.
	 *
	 * @param dialog The dialog to setup.
	 */
	protected void setupDialog(Dialog<R> dialog) {
		// Nothing to do here
	}

	/**
	 * Lookup a dialog button.
	 *
	 * @param buttonType The button type to look up.
	 * @return The button node or {@code null} if the button type has not been created.
	 * @see DialogPane#lookupButton(ButtonType)
	 */
	protected final Node lookupButton(ButtonType buttonType) {
		return getUI().getDialogPane().lookupButton(buttonType);
	}

	/**
	 * Register a button event filter.
	 *
	 * @param buttonType The button type to register the filter for.
	 * @param filter The filter to register.
	 */
	protected final void addButtonEventFilter(ButtonType buttonType, EventHandler<? super ActionEvent> filter) {
		getUI().getDialogPane().lookupButton(buttonType).addEventFilter(ActionEvent.ACTION, filter);
	}

	/**
	 * Show the controller's dialog and return.
	 *
	 * @see Dialog#show()
	 */
	public void show() {
		getUI().show();
	}

	/**
	 * Show the controller's dialog and wait until it is closed.
	 *
	 * @return The dialog result.
	 * @see Dialog#showAndWait()
	 */
	public Optional<R> showAndWait() {
		return getUI().showAndWait();
	}

}
