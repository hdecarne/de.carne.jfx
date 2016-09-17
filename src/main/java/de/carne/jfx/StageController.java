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
package de.carne.jfx;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.carne.util.ObjectHolder;
import de.carne.util.logging.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is used for setting up and controlling JavaFX scenes and stages.
 * <p>
 * Stage controller class must conform to the name pattern (.+)Controller and
 * must be derived from this class. In addition a FXML file has to be created
 * for defining the stage content as well as a resource bundle for text
 * resources. All these artifacts are bound together by their names as follows:
 * <ul>
 * <li>MyStageController (controller class)</li>
 * <li>MyStage.fxml (FXML resource defining the stage content)</li>
 * <li>MyStageI18N*.properties (resource bundle)</li>
 * </ul>
 * </p>
 */
public abstract class StageController {

	private static final Log LOG = new Log();

	private static final Pattern CONTROLLER_NAME_PATTERN = Pattern.compile("^(.*)\\.(.+)Controller$");

	private static final ObjectHolder<ScheduledExecutorService> EXECUTOR_SERVICE = new ObjectHolder<>(
			() -> Executors.newSingleThreadScheduledExecutor());

	private ResourceBundle resources = null;

	private Stage stage = null;

	/**
	 * Get the {@link ScheduledExecutorService} shared by all stage controllers.
	 * 
	 * @return The {@link ScheduledExecutorService} to use for background
	 *         processing.
	 */
	protected static ScheduledExecutorService getExecutorService() {
		return EXECUTOR_SERVICE.get();
	}

	void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	/**
	 * Get the stage's resource bundle.
	 *
	 * @return The stage's resource bundle.
	 */
	protected ResourceBundle getResources() {
		return this.resources;
	}

	void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Get the stage.
	 *
	 * @return The stage.
	 */
	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Set up the JavaFX application's primary stage.
	 * <p>
	 * This function is used to setup the JavaFX application's primary stage as
	 * submitted to {@link Application#start(Stage)}.
	 * </p>
	 *
	 * @param primaryStage The primary stage to setup.
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is linked to the submitted
	 *         primary stage.
	 * @throws IOException if an I/O error occurs during stage setup.
	 */
	public static <T extends StageController> T setupPrimaryState(Stage primaryStage, Class<T> controllerClass)
			throws IOException {
		assert primaryStage != null;
		assert controllerClass != null;

		return setupStage(null, primaryStage, controllerClass);
	}

	private static <T extends StageController> T setupStage(Stage ownerStage, Stage controllerStage,
			Class<T> controllerClass) throws IOException {

		String controllerName = controllerClass.getName();

		LOG.debug("Setting up stage for controller: {0}", controllerName);

		Matcher controllerNameMatcher = CONTROLLER_NAME_PATTERN.matcher(controllerName);

		if (!controllerNameMatcher.find()) {
			throw new IllegalArgumentException("Invalid controller class name: " + controllerName);
		}

		String baseName = controllerNameMatcher.group(2);
		String fxmlResourceName = baseName + ".fxml";
		String packageName = controllerNameMatcher.group(1);
		String bundleName = packageName + "." + baseName + "I18N";
		FXMLLoader loader = new FXMLLoader(controllerClass.getResource(fxmlResourceName),
				ResourceBundle.getBundle(bundleName));

		controllerStage.setScene(new Scene(loader.load()));

		T controller = loader.getController();

		controller.setResources(loader.getResources());
		controller.setStage(controllerStage);
		controllerStage.initStyle(controller.getStyle());
		if (ownerStage != null) {
			controllerStage.initOwner(ownerStage);
			controllerStage.initModality(controller.getModality());
		} else {
			controller.setSystemMenuBar();
		}
		controllerStage.setResizable(controller.getResizable());
		controller.setupStage(controllerStage);
		return controller;
	}

	/**
	 * This function is called on root stages to determine the system menu bar.
	 * <p>
	 * This default implementation searches the stage scene's top level nodes
	 * for a {@link MenuBar} node and if found, makes it the system's menu bar.
	 * </p>
	 *
	 * @see MenuBar#setUseSystemMenuBar(boolean)
	 */
	protected void setSystemMenuBar() {
		for (Node node : this.stage.getScene().getRoot().getChildrenUnmodifiable()) {
			if (node instanceof MenuBar) {
				((MenuBar) node).setUseSystemMenuBar(true);
				break;
			}
		}
	}

	/**
	 * This function is called during stage initialization to determine the
	 * stages's style.
	 * <p>
	 * The default style is {@link StageStyle#DECORATED}.
	 * </p>
	 *
	 * @return The stage style to use.
	 * @see StageStyle
	 */
	protected StageStyle getStyle() {
		return StageStyle.DECORATED;
	}

	/**
	 * This function is called during stage initialization to determine the
	 * stages's modality.
	 * <p>
	 * The default modality is {@link Modality#WINDOW_MODAL}.
	 * </p>
	 *
	 * @return The modality to use.
	 * @see Modality
	 */
	protected Modality getModality() {
		return Modality.WINDOW_MODAL;
	}

	/**
	 * This function is called during stage initialization to determine whether
	 * the stage is resizable.
	 * <p>
	 * The default is to make the stage resizable.
	 * </p>
	 *
	 * @return {@code true}, if the stage is resizable.
	 */
	protected boolean getResizable() {
		return true;
	}

	/**
	 * This function is called during stage initialization to perform the actual
	 * stage setup.
	 *
	 * @param stage The stage to setup.
	 */
	protected void setupStage(Stage stage) {
		// Nothing to do here
	}

	/**
	 * Get the {@link Preferences} object associated with this stage.
	 * <p>
	 * Override this function to associate a {@link Preferences} object with
	 * this stage.
	 * </p>
	 *
	 * @return The {@link Preferences} object associated with this stage or
	 *         {@code null} if there is none.
	 */
	protected Preferences getPreferences() {
		return null;
	}

	/**
	 * Show the controller's stage and return.
	 *
	 * @see Stage#show()
	 */
	public void show() {
		this.stage.sizeToScene();
		this.stage.show();
	}

	/**
	 * Show the controller's stage and wait until it is closed.
	 *
	 * @see Stage#showAndWait()
	 */
	public void showAndWait() {
		this.stage.sizeToScene();
		this.stage.showAndWait();
	}

	/**
	 * Close the controller's stage.
	 *
	 * @param sync Flag to control whether associated data (preferences) should
	 *        synced.
	 * @see Stage#close()
	 * @see #syncPreferences()
	 */
	public void close(boolean sync) {
		if (sync) {
			syncPreferences();
		}
		this.stage.close();
	}

	/**
	 * Sync any {@link Preferences} object associated with this stage.
	 * <p>
	 * Any error encountered during syncing will be logged and discarded.
	 * </p>
	 *
	 * @see Preferences#sync()
	 */
	public void syncPreferences() {
		Preferences preferences = getPreferences();

		if (preferences != null) {
			try {
				preferences.sync();
			} catch (BackingStoreException e) {
				LOG.warning(e, "An error occurred while syncing preferences ''{0}''", preferences);
			}
		}
	}

}
