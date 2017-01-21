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
package de.carne.jfx.application;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import de.carne.OS;
import de.carne.util.Exceptions;
import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * Utility class providing {@link Platform} related functions.
 */
public final class PlatformHelper {

	private PlatformHelper() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Wrap a {@link Runnable} to make sure it is always invoked on the JavaFX application thread.
	 *
	 * @param runnable The {@link Runnable} to invoke on the JavaFX application thread.
	 * @return The wrapped {@link Runnable}.
	 */
	public static Runnable runLaterRunnable(Runnable runnable) {
		return () -> {
			if (Platform.isFxApplicationThread()) {
				runnable.run();
			} else {
				CountDownLatch latch = new CountDownLatch(1);

				Platform.runLater(() -> {
					try {
						runnable.run();
					} finally {
						latch.countDown();
					}
				});
				try {
					latch.await();
				} catch (InterruptedException e) {
					Exceptions.ignore(e);
					Thread.currentThread().interrupt();
				}
			}
		};
	}

	/**
	 * Make sure a {@link Supplier} is always invoked on the JavaFX application thread.
	 *
	 * @param <R> The actual result type.
	 * @param supplier The supplier to invoke.
	 * @return The supplier result.
	 */
	public static <R> R runLater(Supplier<R> supplier) {
		R result;

		if (Platform.isFxApplicationThread()) {
			result = supplier.get();
		} else {
			CountDownLatch latch = new CountDownLatch(1);
			AtomicReference<R> resultHolder = new AtomicReference<>(null);

			Platform.runLater(() -> {
				try {
					resultHolder.set(supplier.get());
				} finally {
					latch.countDown();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				Exceptions.ignore(e);
				Thread.currentThread().interrupt();
			}
			result = resultHolder.get();
		}
		return result;
	}

	/**
	 * Make sure a {@link Runnable} is always invoked on the JavaFX application thread.
	 *
	 * @param runnable The runnable to invoke.
	 */
	public static void runLater(Runnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			CountDownLatch latch = new CountDownLatch(1);

			Platform.runLater(() -> {
				try {
					runnable.run();
				} finally {
					latch.countDown();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				Exceptions.ignore(e);
				Thread.currentThread().interrupt();
			}
		}
	}

	private static final Image[] EMPTY_ICONS = new Image[0];

	/**
	 * Filter stage icons according to platform preference.
	 *
	 * @param icons The available icons.
	 * @return The filtered icons.
	 */
	public static Image[] stageIcons(Image... icons) {
		if (OS.IS_MACOS) {
			return EMPTY_ICONS;
		}
		return icons;
	}

	/**
	 * Filter stage icons according to platform preference.
	 *
	 * @param icons The available icons.
	 * @return The filtered icons.
	 */
	public static Collection<Image> stageIcons(Collection<Image> icons) {
		if (OS.IS_MACOS) {
			return Collections.emptyList();
		}
		return icons;
	}

}
