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
package de.carne.jfx.application;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;

/**
 * Utility class providing {@link Platform} related functions.
 */
public final class PlatformHelper {

	/**
	 * Wrap a {@link Runnable} to make sure it is always invoked on the JavaFX
	 * application thread.
	 *
	 * @param runnable The {@link Runnable} to invoke on the JavaFX application
	 *        thread.
	 * @return The wrapped {@link Runnable}.
	 */
	public static Runnable runLater(Runnable runnable) {
		return new Runnable() {

			@Override
			public void run() {
				if (Platform.isFxApplicationThread()) {
					runnable.run();
				} else {
					CountDownLatch latch = new CountDownLatch(1);

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							try {
								runnable.run();
							} finally {
								latch.countDown();
							}
						}

					});
					try {
						latch.await();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}

		};
	}

}
