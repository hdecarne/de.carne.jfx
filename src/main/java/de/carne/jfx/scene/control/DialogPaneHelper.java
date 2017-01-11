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

import de.carne.jfx.stage.logview.LogViewFormats;
import de.carne.jfx.stage.logview.LogViewImages;
import de.carne.util.Exceptions;
import javafx.collections.FXCollections;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * Utility class providing {@link DialogPane} related functions.
 */
public final class DialogPaneHelper {

	private DialogPaneHelper() {
		// Make sure this class is not instantiated from outside
	}

	private static final double IMAGE_SIZE16 = 16.0;

	/**
	 * Add an {@link Exception}'s stack trace to a {@link DialogPane} as an
	 * expandable content.
	 *
	 * @param dialogPane The dialog pane to add the exception to.
	 * @param exception The exception to add (may be {@code null}).
	 * @see DialogPane#setExpandableContent(javafx.scene.Node)
	 */
	public static void setExceptionContent(DialogPane dialogPane, Throwable exception) {
		assert dialogPane != null;

		if (exception != null) {
			TextArea traceView = new TextArea(Exceptions.getStackTrace(exception));

			traceView.setEditable(false);
			traceView.setBackground(dialogPane.getBackground());

			AnchorPane traceViewPane = new AnchorPane(traceView);

			AnchorPane.setLeftAnchor(traceView, 0.0);
			AnchorPane.setTopAnchor(traceView, 0.0);
			AnchorPane.setRightAnchor(traceView, 0.0);
			AnchorPane.setBottomAnchor(traceView, 0.0);
			dialogPane.setExpandableContent(traceViewPane);
		}
	}

	/**
	 * Add a {@link LogRecord} list view to a {@link DialogPane} as an
	 * expandable content.
	 *
	 * @param dialogPane The dialog pane to add the log records to.
	 * @param logRecords The log records to add (may be {@code null}).
	 * @see DialogPane#setExpandableContent(javafx.scene.Node)
	 */
	public static void setLogRecordsContent(DialogPane dialogPane, Collection<LogRecord> logRecords) {
		assert dialogPane != null;

		if (logRecords != null && !logRecords.isEmpty()) {
			ListView<LogRecord> logView = new ListView<>(FXCollections.observableArrayList(logRecords));

			logView.setCellFactory(new Callback<ListView<LogRecord>, ListCell<LogRecord>>() {

				@Override
				public ListCell<LogRecord> call(ListView<LogRecord> param) {
					return new ListCell<LogRecord>() {

						@Override
						protected void updateItem(LogRecord item, boolean empty) {
							super.updateItem(item, empty);
							if (item != null) {
								setText(LogViewFormats.MESSAGE_FORMAT.format(item));

								Image itemImage = LogViewImages.LEVEL_IMAGES.getImage(item.getLevel(), IMAGE_SIZE16);

								if (itemImage != null) {
									setGraphic(new ImageView(itemImage));
								}
							}
						}

					};
				}

			});
			logView.setBackground(dialogPane.getBackground());

			AnchorPane logViewPane = new AnchorPane(logView);

			AnchorPane.setLeftAnchor(logView, 0.0);
			AnchorPane.setTopAnchor(logView, 0.0);
			AnchorPane.setRightAnchor(logView, 0.0);
			AnchorPane.setBottomAnchor(logView, 0.0);
			dialogPane.setExpandableContent(logViewPane);
		}
	}

}
