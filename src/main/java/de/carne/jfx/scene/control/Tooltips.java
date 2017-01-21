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

import de.carne.check.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.PopupWindow.AnchorLocation;

/**
 * Utility class providing {@link Tooltip} related functions.
 */
public final class Tooltips {

	private Tooltips() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Show a tool tip for a specific region.
	 *
	 * @param region The region to display the tool tip for.
	 * @param message The message to display:
	 * @return The created tool tip.
	 */
	public static Tooltip show(Region region, String message) {
		return show(region, message, null);
	}

	/**
	 * Show a tool tip for a specific region.
	 *
	 * @param region The region to display the tool tip for.
	 * @param message The message to display:
	 * @param image The image to display (may be {@code null}).
	 * @return The created tool tip.
	 */
	public static Tooltip show(Region region, String message, @Nullable Image image) {
		Tooltip tooltip = new Tooltip(message);

		if (image != null) {
			tooltip.setGraphic(new ImageView(image));
		}
		tooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
		tooltip.setAutoHide(true);

		Point2D position = region.localToScreen(0.0, region.getHeight());

		tooltip.show(region.getScene().getWindow(), position.getX(), position.getY());
		return tooltip;
	}

}
