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
package de.carne.jfx.scene.control;

import java.util.Comparator;

import de.carne.util.DefaultSet;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Utility class providing convenience functions for all kind of UI controls.
 */
public final class Controls {

	private Controls() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Reset a {@link ComboBox}'s content and selection.
	 *
	 * @param <T> The {@link ComboBox}'s element type.
	 * @param control The {@link ComboBox} to reset.
	 * @param defaultSet The {@link DefaultSet} to apply.
	 * @param comparator The {@link Comparator} to use for combobox item sorting.
	 */
	public static <T> void resetComboBoxOptions(ComboBox<T> control, DefaultSet<T> defaultSet,
			Comparator<T> comparator) {
		ObservableList<T> options = control.getItems();

		options.clear();
		if (!defaultSet.isEmpty()) {
			options.addAll(defaultSet);
			options.sort(comparator);
			control.setValue(defaultSet.getDefault());
			if (!control.disableProperty().isBound()) {
				control.setDisable(false);
			}
		} else {
			if (!control.disableProperty().isBound()) {
				control.setDisable(!control.isEditable());
			}
		}
	}

}
