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
package de.carne.jfx.scene.control.aboutinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import de.carne.boot.check.Nullable;
import de.carne.jfx.scene.control.DialogController;
import de.carne.util.Exceptions;
import de.carne.util.ManifestInfos;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * About info window.
 */
public class AboutInfoController extends DialogController<ButtonType> {

	@SuppressWarnings("null")
	@FXML
	private TabPane ctlInfoTabs;

	@Override
	protected void setupDialog(Dialog<ButtonType> dialog) {
		dialog.setTitle(AboutInfoI18N.formatSTR_STAGE_TITLE(ManifestInfos.APPLICATION_NAME));
		dialog.setHeaderText(AboutInfoI18N.formatSTR_TEXT_HEADER(ManifestInfos.APPLICATION_NAME,
				ManifestInfos.APPLICATION_VERSION, ManifestInfos.APPLICATION_BUILD));
	}

	/**
	 * Set the application logo.
	 *
	 * @param image The logo image to display.
	 * @return This controller.
	 */
	public AboutInfoController setLogo(Image image) {
		getUI().setGraphic(new ImageView(image));
		return this;
	}

	/**
	 * Add an info message to the dialog.
	 * <p>
	 * The first line of the submitted text resource is used as a title. The remaining lines are displayed as a single
	 * text.
	 * <p>
	 * If the submitted URL is {@code null} or cannot be read, no info message is added to the dialog.
	 *
	 * @param url The URL of the text resource to add.
	 * @return This controller.
	 */
	public AboutInfoController addInfo(@Nullable URL url) {
		if (url != null) {
			try (BufferedReader infoReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
				String infoTitle = infoReader.readLine();

				if (infoTitle == null) {
					throw new IllegalArgumentException("Empty info resource: " + url);
				}

				StringBuilder infoTextBuffer = new StringBuilder();
				String infoTextLine;

				while ((infoTextLine = infoReader.readLine()) != null) {
					infoTextBuffer.append(infoTextLine).append('\n');
				}

				TextArea infoTextArea = new TextArea(infoTextBuffer.toString());

				infoTextArea.setEditable(false);

				AnchorPane infoTextPane = new AnchorPane(infoTextArea);

				AnchorPane.setLeftAnchor(infoTextArea, 0.0);
				AnchorPane.setTopAnchor(infoTextArea, 0.0);
				AnchorPane.setRightAnchor(infoTextArea, 0.0);
				AnchorPane.setBottomAnchor(infoTextArea, 0.0);
				this.ctlInfoTabs.getTabs().add(new Tab(infoTitle, infoTextPane));
			} catch (IOException e) {
				Exceptions.warn(e);
			}
		}
		return this;
	}

}
