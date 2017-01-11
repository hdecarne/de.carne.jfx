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
package de.carne.jfx.scene.control.cell;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * {@link TableCell} implementation for drawing {@link Image} values.
 *
 * @param <S> The type displayed in the containing table view.
 */
public class ImageViewTableCell<S> extends TableCell<S, Image> {

	private final ImageView imageView = new ImageView();

	/**
	 * Create a cell factory for use in a {@link TableColumn}.
	 *
	 * @param <T> The actual cell value type.
	 * @return A cell factory for creating image based cells.
	 */
	public static <T> Callback<TableColumn<T, Image>, TableCell<T, Image>> forTableColumn() {
		return new Callback<TableColumn<T, Image>, TableCell<T, Image>>() {

			@Override
			public TableCell<T, Image> call(TableColumn<T, Image> col) {
				return new ImageViewTableCell<>();
			}

		};
	}

	@Override
	protected void updateItem(Image item, boolean empty) {
		if (empty) {
			setGraphic(null);
		} else {
			this.imageView.setImage(item);
			setGraphic(this.imageView);
		}
	}

}
