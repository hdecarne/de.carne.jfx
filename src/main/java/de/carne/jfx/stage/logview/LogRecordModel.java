/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.logging.LogRecord;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * Model class for {@link LogRecord} objects.
 */
public class LogRecordModel {

	private final ObjectProperty<Image> levelProperty;
	private final ObjectProperty<LogRecordDate> timeProperty;
	private final StringProperty threadProperty;
	private final StringProperty messageProperty;

	/**
	 * Construct {@link LogRecord}.
	 * 
	 * @param record The represented log record.
	 */
	public LogRecordModel(LogRecord record) {
		this.levelProperty = new SimpleObjectProperty<>(LogViewImages.LEVEL_IMAGES.getImage(record.getLevel(), 16.0));
		this.timeProperty = new SimpleObjectProperty<>(new LogRecordDate(record.getMillis()));
		this.threadProperty = new SimpleStringProperty(Integer.toString(record.getThreadID()));
		this.messageProperty = new SimpleStringProperty(LogViewFormats.MESSAGE_FORMAT.format(record));
	}

	/**
	 * Get the record's log level.
	 *
	 * @return The record's log level.
	 */
	public Image getLevel() {
		return this.levelProperty.get();
	}

	/**
	 * Set the record's log level.
	 *
	 * @param level The log level to set.
	 */
	public void setLevel(Image level) {
		this.levelProperty.set(level);
	}

	/**
	 * Get the record's level property.
	 *
	 * @return The record's level property.
	 */
	public ObjectProperty<Image> levelProperty() {
		return this.levelProperty;
	}

	/**
	 * Get the record's log time.
	 *
	 * @return The record's log time.
	 */
	public LogRecordDate getTime() {
		return this.timeProperty.get();
	}

	/**
	 * Set the record's log time.
	 *
	 * @param time The log time to set.
	 */
	public void setTime(LogRecordDate time) {
		this.timeProperty.set(time);
	}

	/**
	 * Get the record's time property.
	 *
	 * @return The record's time property.
	 */
	public ObjectProperty<LogRecordDate> timeProperty() {
		return this.timeProperty;
	}

	/**
	 * Get the record's log thread.
	 *
	 * @return The record's log thread.
	 */
	public String getThread() {
		return this.threadProperty.get();
	}

	/**
	 * Set the record's log thread.
	 *
	 * @param thread The log thread to set.
	 */
	public void setThread(String thread) {
		this.threadProperty.set(thread);
	}

	/**
	 * Get the record's thread property.
	 *
	 * @return The record's thread property.
	 */
	public StringProperty threadProperty() {
		return this.threadProperty;
	}

	/**
	 * Get the record's log message.
	 *
	 * @return The record's log message.
	 */
	public String getMessage() {
		return this.messageProperty.get();
	}

	/**
	 * Set the record's log message.
	 *
	 * @param message The log message to set.
	 */
	public void setMessage(String message) {
		this.messageProperty.set(message);
	}

	/**
	 * Get the record's message property.
	 *
	 * @return The record's message property.
	 */
	public StringProperty messageProperty() {
		return this.messageProperty;
	}

}
