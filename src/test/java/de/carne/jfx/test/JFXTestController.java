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
package de.carne.jfx.test;

import java.io.IOException;

import de.carne.jfx.scene.control.Alerts;
import de.carne.jfx.scene.control.aboutinfo.AboutInfoDialog;
import de.carne.jfx.stage.StageController;
import de.carne.jfx.stage.logview.LogViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Test main window.
 */
public class JFXTestController extends StageController {

	@FXML
	void onCmdClose(ActionEvent evt) {
		close(true);
	}

	@FXML
	void onCmdLogs(ActionEvent evt) {
		try {
			loadStage(LogViewController.class).show();
		} catch (IOException e) {
			Alerts.unexpected(e).showAndWait();
		}
	}

	@FXML
	void onCmdAbout(ActionEvent evt) {
		try {
			AboutInfoDialog.load(this).showAndWait();
		} catch (IOException e) {
			Alerts.unexpected(e).showAndWait();
		}
	}

}
