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
package de.carne.jfx.stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import de.carne.jfx.fxml.FXMLController;
import de.carne.jfx.scene.control.DialogController;
import de.carne.util.ObjectHolder;
import de.carne.util.logging.Log;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Controller class for {@link Stage} based scenes.
 *
 * @see FXMLController
 */
public abstract class StageController extends FXMLController<Stage> {

	private static final Log LOG = new Log();

	private static final ObjectHolder<ScheduledExecutorService> EXECUTOR_SERVICE = new ObjectHolder<>(
			() -> Executors.newSingleThreadScheduledExecutor());

	private final AtomicInteger backgroundTaskCount = new AtomicInteger(0);

	/**
	 * Get the {@link ScheduledExecutorService} shared by all stage controllers.
	 *
	 * @return The {@link ScheduledExecutorService} to use for background
	 *         processing.
	 */
	protected static ScheduledExecutorService getExecutorService() {
		return EXECUTOR_SERVICE.get();
	}

	/**
	 * Load the JavaFX application's primary stage.
	 * <p>
	 * This function is used to load and setup the JavaFX application's primary
	 * stage as submitted to {@link Application#start(Stage)}.
	 *
	 * @param primaryStage The primary stage to setup.
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is bound to the submitted
	 *         primary stage.
	 * @throws IOException if an I/O error occurs during stage loading.
	 */
	public static <C extends StageController> C loadPrimaryStage(Stage primaryStage, Class<C> controllerClass)
			throws IOException {
		assert primaryStage != null;
		assert controllerClass != null;

		return loadUI(null, (c) -> primaryStage, controllerClass);
	}

	/**
	 * Load and create new stage.
	 * <p>
	 * The newly created stage is owned by the calling controller's stage.
	 *
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is bound to the newly created
	 *         stage.
	 * @throws IOException if an I/O error occurs during stage loading.
	 */
	public <C extends StageController> C loadStage(Class<C> controllerClass) throws IOException {
		assert controllerClass != null;

		return loadUI(getUI(), (c) -> new Stage(), controllerClass);
	}

	@Override
	public final Window getWindow() {
		return getUI();
	}

	@Override
	protected void setupUI(Window owner, Stage stage, Parent fxmlRoot) {
		stage.setOnCloseRequest((evt) -> onCloseRequest(evt));
		stage.setScene(new Scene(fxmlRoot));
		stage.initStyle(getStyle());
		if (owner != null) {
			stage.initOwner(owner);
			stage.initModality(getModality());
		} else {
			setSystemMenuBar();
		}
		stage.setResizable(getResizable());
		setupStage(stage);
	}

	/**
	 * Load and create new dialog.
	 * <p>
	 * The newly created dialog is owned by the calling controller's stage.
	 *
	 * @param dialogFactory The factory function used to create the actual
	 *        dialog object by invoking it with the constructed controller.
	 * @param controllerClass The controller class to use.
	 * @return The constructed controller which is bound to the newly created
	 *         dialog.
	 * @throws IOException if an I/O error occurs during dialog loading.
	 */
	public <R, C extends DialogController<R>> C loadDialog(Function<C, Dialog<R>> dialogFactory,
			Class<C> controllerClass) throws IOException {
		assert controllerClass != null;

		return DialogController.loadDialog(getUI(), dialogFactory, controllerClass);
	}

	/**
	 * This function is called on root stages to determine the system menu bar.
	 * <p>
	 * This default implementation searches the stage scene's top level nodes
	 * for a {@link MenuBar} node and if found, makes it the system's menu bar.
	 *
	 * @see MenuBar#setUseSystemMenuBar(boolean)
	 */
	protected void setSystemMenuBar() {
		for (Node node : getUI().getScene().getRoot().getChildrenUnmodifiable()) {
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
	 * This function is called to block/unblock this stage when either the first
	 * background task has been scheduled or the last background task has been
	 * finished.
	 *
	 * @param blocked Whether the stage should blocked or unblocked.
	 * @see BackgroundTask
	 */
	protected void setBlocked(boolean blocked) {
		getUI().getScene().getRoot().setDisable(blocked);
	}

	/**
	 * Check whether this stage is currently blocked.
	 * <p>
	 * A stage is considered block when at least one unfinished background task
	 * exists.
	 *
	 * @return {@code true} if at least one unfinished background task exists.
	 * @see BackgroundTask
	 */
	public boolean isBlocked() {
		return this.backgroundTaskCount.get() != 0;
	}

	/**
	 * Get the {@link Preferences} object associated with this stage.
	 * <p>
	 * Override this function to associate a {@link Preferences} object with
	 * this stage.
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
		Stage stage = getUI();

		stage.sizeToScene();
		stage.show();
	}

	/**
	 * Show the controller's stage and wait until it is closed.
	 *
	 * @see Stage#showAndWait()
	 */
	public void showAndWait() {
		Stage stage = getUI();

		stage.sizeToScene();
		stage.showAndWait();
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
		getUI().close();
	}

	/**
	 * Sync any {@link Preferences} object associated with this stage.
	 * <p>
	 * Any error encountered during syncing will be logged and discarded.
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

	private void onCloseRequest(WindowEvent evt) {
		if (isBlocked()) {
			evt.consume();
		}
	}

	final void onTaskScheduled() {
		if (this.backgroundTaskCount.getAndIncrement() == 0) {
			setBlocked(true);
		}
	}

	final void onTaskFinished() {
		if (this.backgroundTaskCount.decrementAndGet() == 0) {
			setBlocked(false);
		}
	}

	/**
	 * Base class for {@link Task} run by this stage.
	 *
	 * @param <V> The tasks result/value type.
	 */
	protected abstract class BackgroundTask<V> extends Task<V> {

		@Override
		protected void scheduled() {
			onTaskScheduled();
		}

		@Override
		protected void succeeded() {
			onTaskFinished();
		}

		@Override
		protected void cancelled() {
			onTaskFinished();
		}

		@Override
		protected void failed() {
			onTaskFinished();
		}

	}

}
