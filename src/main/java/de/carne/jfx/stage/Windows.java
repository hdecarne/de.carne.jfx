/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import javafx.stage.Window;

/**
 * Utility class providing {@link Window} related functions.
 */
public final class Windows {

	private Windows() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Make sure a {@link Consumer} is invoked as soon as a corresponding {@link Window} is about to get closed.
	 *
	 * @param <T> The actual consumer parameter type.
	 * @param window The {@link Window} to monitor.
	 * @param consumer The {@link Consumer} to invoke.
	 * @param t The consumer parameter to use.
	 * @return The submitted consumer parameter.
	 */
	public static <T> T onHiding(Window window, Consumer<T> consumer, T t) {
		window.onHidingProperty().addListener((p, o, n) -> consumer.accept(t));
		return t;
	}

}
