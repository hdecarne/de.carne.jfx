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
package de.carne.jfx.scene.image;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

import de.carne.check.Check;
import de.carne.check.Nullable;
import javafx.scene.image.Image;

/**
 * Utility class providing a {@link NavigableMap} based data structure for registering and retrieving {@link Image}
 * objects depending on their key and size.
 *
 * @param <K> The key type to use.
 */
public final class ImageRegistry<K> {

	private final NavigableMap<CompositeKey, Image> imageMap = new TreeMap<>();

	private final Comparator<K> comparator;

	/**
	 * Construct {@code ImageRegistry}.
	 *
	 * @param comparator The {@link Comparator} to use for key comparison.
	 */
	public ImageRegistry(Comparator<K> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Register an image for a specific key.
	 *
	 * @param key The key to associate the image with.
	 * @param image The image to register.
	 * @return The previously registered image, or {@code null} if no image has yet been registered for the submitted
	 *         key and image size.
	 */
	public Image registerImage(K key, Image image) {
		return this.imageMap.put(new CompositeKey(key, image.getHeight(), image.getWidth()), image);
	}

	/**
	 * Get the image associated with a specific key.
	 * <p>
	 * If multiple images with different sizes have been registered for the submitted key, the largest image is
	 * returned.
	 *
	 * @param key The key to get the image for.
	 * @return The found image, or {@code null} if no image has yet been registered for the submitted key.
	 */
	@Nullable
	public Image getImage(K key) {
		return getImage(key, 0.0, null);
	}

	/**
	 * Get the image associated with a specific key.
	 * <p>
	 * If multiple images with different sizes have been registered for the submitted key, the largest image is
	 * returned.
	 *
	 * @param key The key to get the image for.
	 * @param def The default image to return in case no image has hat been registered for the submitted key.
	 * @return The found image, or the default image if no image has yet been registered for the submitted key.
	 */
	@Nullable
	public Image getImage(K key, @Nullable Image def) {
		return getImage(key, 0.0, def);
	}

	/**
	 * Get the image associated with a specific key and nearest to a given size.
	 *
	 * @param key The key to get the image for.
	 * @param size The size to match by the image.
	 * @return The found image, or {@code null} if no image has yet been registered for the submitted key.
	 */
	@Nullable
	public Image getImage(K key, double size) {
		return getImage(key, size, null);
	}

	/**
	 * Get the image associated with a specific key and nearest to a given size.
	 *
	 * @param key The key to get the image for.
	 * @param size The size to match by the image.
	 * @param def The default image to return in case no image has hat been registered for the submitted key.
	 * @return The found image, or {@code null} if no image has yet been registered for the submitted key.
	 */
	@Nullable
	public Image getImage(K key, double size, @Nullable Image def) {
		CompositeKey searchKey = new CompositeKey(key, size);
		Map.Entry<CompositeKey, Image> entry = this.imageMap.ceilingEntry(searchKey);

		if (entry == null || !entry.getKey().baseKey().equals(key)) {
			entry = this.imageMap.lowerEntry(searchKey);
		}
		return (entry != null && entry.getKey().baseKey().equals(key) ? entry.getValue() : def);
	}

	int compareBaseKey(K o1, K o2) {
		return this.comparator.compare(o1, o2);
	}

	private final class CompositeKey implements Comparable<CompositeKey> {

		private final K baseKey;

		private final int sizeKey;

		public CompositeKey(K baseKey, double height, double width) {
			this.baseKey = baseKey;
			this.sizeKey = (int) (height * width);
		}

		public CompositeKey(K baseKey, double size) {
			this.baseKey = baseKey;
			this.sizeKey = (size > 0.0 ? (int) (size * size) : Integer.MAX_VALUE);
		}

		public K baseKey() {
			return this.baseKey;
		}

		@Override
		public int compareTo(@Nullable CompositeKey _o) {
			CompositeKey o = Check.notNull(_o);

			int comparison = compareBaseKey(this.baseKey, o.baseKey);

			if (comparison == 0) {
				comparison = Integer.compare(this.sizeKey, o.sizeKey);
			}
			return comparison;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.baseKey, this.sizeKey);
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			boolean equal = false;

			if (this == obj) {
				equal = true;
			} else if (obj instanceof ImageRegistry<?>.CompositeKey) {
				ImageRegistry<?>.CompositeKey o = (ImageRegistry<?>.CompositeKey) obj;

				equal = this.baseKey.equals(o.baseKey) && this.sizeKey == o.sizeKey;
			}
			return equal;
		}

	}

}
