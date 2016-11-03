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

import de.carne.jfx.stage.StageController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Log view window.
 */
public class LogViewController extends StageController {

	@FXML
	TableView<?> ctlLogRecords;

	@FXML
	TableColumn<?, ?> ctlLogRecordLevel;

	@FXML
	TableColumn<?, ?> ctlLogRecordTime;

	@FXML
	TableColumn<?, ?> ctlLogRecordThread;

	@FXML
	TableColumn<?, ?> ctlLogRecordMessage;

}
