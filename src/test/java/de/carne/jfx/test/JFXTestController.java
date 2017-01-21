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
import java.net.URL;

import de.carne.jfx.scene.control.Alerts;
import de.carne.jfx.scene.control.aboutinfo.AboutInfoDialog;
import de.carne.jfx.stage.StageController;
import de.carne.jfx.stage.logview.LogViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

/**
 * Test main window.
 */
public class JFXTestController extends StageController {

	@SuppressWarnings("unused")
	@FXML
	private void onCmdClose(ActionEvent evt) {
		close(true);
	}

	@SuppressWarnings("unused")
	@FXML
	private void onCmdLogs(ActionEvent evt) {
		try {
			loadStage(LogViewController.class).show();
		} catch (IOException e) {
			Alerts.unexpected(e).showAndWait();
		}
	}

	@SuppressWarnings("unused")
	@FXML
	private void onCmdAbout(ActionEvent evt) {
		try {
			Image logo = new Image(getClass().getResource("logo.png").toExternalForm());
			URL info = getClass().getResource("info.txt");

			AboutInfoDialog.load(this).setLogo(logo).addInfo(info).showAndWait();
		} catch (IOException e) {
			Alerts.unexpected(e).showAndWait();
		}
	}

}
