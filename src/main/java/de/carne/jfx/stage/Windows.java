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

import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Utility class providing {@link Window} related functions.
 */
public final class Windows {

	/**
	 * Make sure a {@link Consumer} is invoked as soon as a corresponding
	 * {@link Window} is about to get closed.
	 *
	 * @param window The {@link Window} to monitor.
	 * @param consumer The {@link Consumer} to invoke.
	 * @param t The consumer parameter to use.
	 * @return The submitted consumer parameter.
	 */
	public static <T> T onHiding(Window window, Consumer<T> consumer, T t) {
		assert window != null;
		assert consumer != null;

		window.onHidingProperty().addListener(new ChangeListener<EventHandler<WindowEvent>>() {

			@Override
			public void changed(ObservableValue<? extends EventHandler<WindowEvent>> observable,
					EventHandler<WindowEvent> oldValue, EventHandler<WindowEvent> newValue) {
				consumer.accept(t);
			}

		});
		return t;
	}

}
