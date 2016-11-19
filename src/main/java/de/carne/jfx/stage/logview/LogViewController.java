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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.checkerframework.checker.nullness.qual.Nullable;

import de.carne.jfx.application.PlatformHelper;
import de.carne.jfx.scene.control.Alerts;
import de.carne.jfx.scene.control.cell.ImageViewTableCell;
import de.carne.jfx.stage.StageController;
import de.carne.jfx.util.FileChooserHelper;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogBuffer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Log view window.
 */
public class LogViewController extends StageController {

	private static final Log LOG = new Log();

	private final Handler logHandler = new Handler() {

		@Override
		public void publish(LogRecord record) {
			PlatformHelper.runLater(() -> onPublish(record));
		}

		@Override
		public void flush() {
			// Nothing to do
		}

		@Override
		public void close() throws SecurityException {
			// Nothing to do
		}

	};

	private final ChangeListener<Boolean> toggleListener = (p, o, n) -> onToggled(n.booleanValue());

	private @Nullable BooleanProperty toggleProperty = null;

	@FXML
	TableView<LogRecordModel> ctlLogRecords;

	@FXML
	TableColumn<LogRecordModel, Image> ctlLogRecordLevel;

	@FXML
	TableColumn<LogRecordModel, LogRecordDate> ctlLogRecordTime;

	@FXML
	TableColumn<LogRecordModel, String> ctlLogRecordThread;

	@FXML
	TableColumn<LogRecordModel, String> ctlLogRecordMessage;

	@FXML
	void onCmdClear(ActionEvent evt) {
		LogBuffer.clear(LOG.getLogger());
		this.ctlLogRecords.getItems().clear();
		LOG.notice("Log buffer cleared");
	}

	@FXML
	void onCmdExport(ActionEvent evt) {
		FileChooser chooser = new FileChooser();
		List<ExtensionFilter> extensionFilters = new ArrayList<>();

		extensionFilters.add(FileChooserHelper.filterFromString(LogViewI18N.formatSTR_FILTER_LOGFILES()));
		extensionFilters.add(FileChooserHelper.filterFromString(LogViewI18N.formatSTR_FILTER_TXTFILES()));
		extensionFilters.add(FileChooserHelper.filterFromString(LogViewI18N.formatSTR_FILTER_ALLFILES()));
		chooser.getExtensionFilters().addAll(extensionFilters);
		chooser.setSelectedExtensionFilter(extensionFilters.get(0));

		File file = chooser.showSaveDialog(getUI());

		if (file != null) {
			LOG.info("Exporting log buffer to file: ''{0}''...", file);
			try {
				LogBuffer.exportTo(LOG.getLogger(), file);
			} catch (IOException e) {
				Alerts.unexpected(e).showAndWait();
			}
		}

	}

	Void onPublish(LogRecord record) {
		ObservableList<LogRecordModel> records = this.ctlLogRecords.getItems();

		while (records.size() >= LogBuffer.BUFFER_LIMIT) {
			records.remove(0);
		}
		records.add(new LogRecordModel(record));

		int selectedIndex = this.ctlLogRecords.getSelectionModel().getSelectedIndex();

		selectedIndex = (selectedIndex < 0 ? 0 : selectedIndex + 1);
		if (selectedIndex + 1 == records.size()) {
			this.ctlLogRecords.getSelectionModel().select(selectedIndex);
			this.ctlLogRecords.scrollTo(selectedIndex);
		}
		return null;
	}

	private void onShowingChanged(boolean showing) {
		if (showing) {
			LogBuffer.addHandler(LOG.getLogger(), this.logHandler);
		} else {
			LogBuffer.removeHandler(LOG.getLogger(), this.logHandler);
			setToggle(null);
		}
	}

	private void onToggled(boolean selected) {
		if (!selected) {
			close(false);
		}
	}

	@Override
	protected StageStyle getStyle() {
		return StageStyle.UTILITY;
	}

	@Override
	protected Modality getModality() {
		return Modality.NONE;
	}

	@Override
	protected void setupStage(Stage stage) {
		stage.setTitle(LogViewI18N.formatSTR_STAGE_TITLE());
		this.ctlLogRecordLevel.setCellFactory(ImageViewTableCell.forTableColumn());
		this.ctlLogRecordLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
		this.ctlLogRecordTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		this.ctlLogRecordThread.setCellValueFactory(new PropertyValueFactory<>("thread"));
		this.ctlLogRecordMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
		stage.showingProperty().addListener((p, o, n) -> onShowingChanged(n.booleanValue()));
	}

	/**
	 * Set a toggle property which is cleared when the log view closes and
	 * which's toggling also closes the log view.
	 *
	 * @param toggleProperty The toggle property to set.
	 * @return This log view for chaining.
	 */
	public LogViewController setToggle(BooleanProperty toggleProperty) {
		if (this.toggleProperty != null) {
			this.toggleProperty.removeListener(this.toggleListener);
			this.toggleProperty.set(false);
		}
		this.toggleProperty = toggleProperty;
		if (this.toggleProperty != null) {
			this.toggleProperty.addListener(this.toggleListener);
		}
		return this;
	}

}
