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
package de.carne.jfx.fxml;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.carne.util.logging.Log;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

/**
 * This class is used for setting up and controlling FXML based scenes.
 * <p>
 * Controller classes must conform to the name pattern (.+)Controller and must
 * be derived from one of this class' subclasses. In addition a FXML file has to
 * be created for defining the scene content as well as a resource bundle for
 * text resources. All these artifacts are bound together by their names as
 * follows:
 * <ul>
 * <li>MyStageController (controller class)</li>
 * <li>MyStage.fxml (FXML resource defining the stage content)</li>
 * <li>MyStageI18N*.properties (resource bundle)</li>
 * </ul>
 *
 * @param <U> The actual JavaFX UI type.
 */
public abstract class FXMLController<U> {

	private static final Log LOG = new Log();

	private static final Pattern CONTROLLER_NAME_PATTERN = Pattern.compile("^(.*)\\.(.+)Controller$");

	private ResourceBundle resources = null;

	private U ui = null;

	final void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	/**
	 * Get the UI resource bundle.
	 *
	 * @return The UI resource bundle.
	 */
	protected final ResourceBundle getResources() {
		return this.resources;
	}

	final void setUI(U ui) {
		this.ui = ui;
	}

	/**
	 * Get the UI object.
	 *
	 * @return The UI object.
	 */
	public final U getUI() {
		return this.ui;
	}

	/**
	 * Perform the basic UI setup by loading the UI resource bundle, the scene
	 * content as well as the controller class and bind all together.
	 *
	 * @param owner The UI object's owner (may by {@code null}).
	 * @param uiFactory The factory function used to create the actual UI object
	 *        by invoking it with the constructed controller.
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is bound to the constructed
	 *         scene and UI object.
	 * @throws IOException if an I/O error occurs during stage setup.
	 */
	protected static <U, C extends FXMLController<U>> C loadUI(Window owner, Function<C, U> uiFactory,
			Class<C> controllerClass) throws IOException {

		String controllerName = controllerClass.getName();

		LOG.debug("Loading UI for controller: {0}", controllerName);

		Matcher controllerNameMatcher = CONTROLLER_NAME_PATTERN.matcher(controllerName);

		if (!controllerNameMatcher.find()) {
			throw new IllegalArgumentException("Invalid controller class name: " + controllerName);
		}

		String baseName = controllerNameMatcher.group(2);
		String fxmlResourceName = baseName + ".fxml";
		String packageName = controllerNameMatcher.group(1);
		String bundleName = packageName + "." + baseName + "I18N";
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		FXMLLoader loader = new FXMLLoader(controllerClass.getResource(fxmlResourceName), bundle);
		Parent fxmlRoot = loader.load();
		C controller = loader.getController();

		controller.setResources(loader.getResources());

		U ui = uiFactory.apply(controller);

		controller.setUI(ui);
		controller.setupUI(owner, ui, fxmlRoot);
		return controller;
	}

	/**
	 * This function is called during UI initialization to perform the actual UI
	 * setup.
	 *
	 * @param owner The UI object's owner (may by {@code null}).
	 * @param ui The constructed UI object.
	 * @param fxmlRoot The scene's root node as defined by the FXML resource.
	 */
	protected abstract void setupUI(Window owner, U ui, Parent fxmlRoot);

}
