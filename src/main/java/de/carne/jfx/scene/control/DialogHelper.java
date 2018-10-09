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

import org.eclipse.jdt.annotation.Nullable;

import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

/**
 * Utility class providing {@link Dialog} related functions.
 */
public class DialogHelper {

	private DialogHelper() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Add an {@link Exception}'s stack trace to a {@link Dialog}'s {@link DialogPane} as an expandable content.
	 *
	 * @param <R> The dialog's result type.
	 * @param <T> The actual {@link Dialog} type.
	 * @param dialog The dialog to add the exception to.
	 * @param throwable The exception to add (may be {@code null}).
	 * @return The updated dialog.
	 * @see DialogPane#setExpandableContent(javafx.scene.Node)
	 */
	public static <R, T extends Dialog<R>> T setExceptionContent(T dialog, @Nullable Throwable throwable) {
		DialogPaneHelper.setExceptionContent(dialog.getDialogPane(), throwable);
		return dialog;
	}

	/**
	 * Add a collection of {@link LogRecord}'s to a {@link Dialog}'s {@link DialogPane} as an expandable content.
	 *
	 * @param <R> The dialog's result type.
	 * @param <T> The actual {@link Dialog} type.
	 * @param dialog The dialog to add the exception to.
	 * @param logs The collection of log records to add (may by {@code null}).
	 * @return The updated dialog.
	 * @see DialogPane#setExpandableContent(javafx.scene.Node)
	 */
	public static <R, T extends Dialog<R>> T setLogRecordsContent(T dialog, Collection<LogRecord> logs) {
		DialogPaneHelper.setLogRecordsContent(dialog.getDialogPane(), logs);
		return dialog;
	}

}
