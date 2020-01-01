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
package de.carne.jfx.test;

import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TextMatchers;

import de.carne.boot.logging.Logs;

/**
 * TestFX based test case responsible for performing all JavaFX based tests.
 */
public class JFXTest extends FxRobot {

	/**
	 * Set this system property to {@code true} to not use headless mode.
	 */
	public static final String PROPERTY_NO_HEADLESS_MODE = "noHeadlessMode";

	/**
	 * Setup TestFX.
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpTestFX() throws Exception {
		System.setProperty("enableTestMode", "true");
		Logs.readConfig(Logs.CONFIG_DEBUG);
		Locale.setDefault(Locale.US);
		if (!Boolean.getBoolean(PROPERTY_NO_HEADLESS_MODE)) {
			System.out.println("Using headless mode...");
			System.setProperty("testfx.robot", "glass");
			System.setProperty("testfx.headless", "true");
			System.setProperty("prism.order", "sw");
			System.setProperty("prism.text", "t2k");
			System.setProperty("java.awt.headless", "true");
		}
		FxToolkit.registerPrimaryStage();
	}

	/**
	 * Start JavaFX test application.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FxToolkit.setupApplication(JFXTestApplication.class);
	}

	/**
	 * Run all test scenarios in proper order.
	 */
	@Test
	public void testScenarios() {
		scenarioOpened();
		scenarioLogs();
		scenarioAbout();
		scenarioClose();
	}

	private void scenarioOpened() {
		assertRoot();
	}

	private void scenarioLogs() {
		assertRoot();
		clickOn("#menuHelp");
		clickOn("#menuLogs");
		// clickOn(TextMatchers.hasText("Export\u2026"));
		// push(KeyCode.ESCAPE).sleep(100);
		clickOn(TextMatchers.hasText("Clear"));
		clickOn(TextMatchers.hasText("Close"));
	}

	private void scenarioAbout() {
		assertRoot();
		clickOn("#menuHelp");
		clickOn("#menuAbout");
		clickOn(TextMatchers.hasText("Close"));
	}

	private void scenarioClose() {
		assertRoot();
		clickOn("#menuFile");
		clickOn("#menuClose");
		FxAssert.verifyThat("#root", NodeMatchers.isNull());
	}

	private void assertRoot() {
		FxAssert.verifyThat("#root", NodeMatchers.isEnabled());
	}

}
