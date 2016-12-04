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
package de.carne.jfx.scene.control;

import org.checkerframework.checker.nullness.qual.Nullable;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * Utility class for editing {@ListView} elements.
 *
 * @param <T> The list element's type.
 */
public abstract class ListViewEditor<T> {

	private @Nullable ListView<T> listView = null;

	/**
	 * Initialize the editor.
	 *
	 * @param listViewParam The {@ListView} control to use for editing.
	 * @return This editor.
	 */
	public ListViewEditor<T> init(ListView<T> listViewParam) {
		assert listViewParam != null;

		this.listView = listViewParam;
		this.listView.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> setInput(n));
		return this;
	}

	/**
	 * Set a add command button.
	 *
	 * @param cmdButton The add command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setAddCommand(Button cmdButton) {
		assert cmdButton != null;

		cmdButton.setOnAction((evt) -> onAddAction(evt));
		return this;
	}

	/**
	 * Set a apply command button.
	 *
	 * @param cmdButton The apply command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setApplyCommand(Button cmdButton) {
		assert cmdButton != null;

		cmdButton.disableProperty().bind(this.listView.getSelectionModel().selectedItemProperty().isNull());
		cmdButton.setOnAction((evt) -> onApplyAction(evt));
		return this;
	}

	/**
	 * Set a delete command button.
	 *
	 * @param cmdButton The delete command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setDeleteCommand(Button cmdButton) {
		assert cmdButton != null;

		cmdButton.disableProperty().bind(this.listView.getSelectionModel().selectedItemProperty().isNull());
		cmdButton.setOnAction((evt) -> onDeleteAction(evt));
		return this;
	}

	/**
	 * Set a move up command button.
	 *
	 * @param cmdButton The move up command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setMoveUpCommand(Button cmdButton) {
		assert cmdButton != null;

		cmdButton.disableProperty().bind(this.listView.getSelectionModel().selectedItemProperty().isNull());
		cmdButton.setOnAction((evt) -> onMoveUpAction(evt));
		return this;
	}

	/**
	 * Set a move down command button.
	 *
	 * @param cmdButton The move down command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setMoveDownCommand(Button cmdButton) {
		assert cmdButton != null;

		cmdButton.disableProperty().bind(this.listView.getSelectionModel().selectedItemProperty().isNull());
		cmdButton.setOnAction((evt) -> onMoveDownAction(evt));
		return this;
	}

	/**
	 * Called during add or apply action to retrieve the current input.
	 *
	 * @return The entered list element or {@code null} if the current input is
	 *         empty or not valid.
	 */
	protected abstract @Nullable T getInput();

	/**
	 * Called on selection change to initialize the input data with the current
	 * selection.
	 *
	 * @param input The selected list element's data or {@code null} if nothing
	 *        is selected.
	 */
	protected abstract void setInput(@Nullable T input);

	/**
	 * Add action handler.
	 *
	 * @param evt The action event.
	 */
	public void onAddAction(ActionEvent evt) {
		T input = getInput();

		if (input != null) {
			int addIndex = Math.max(0, this.listView.getSelectionModel().getSelectedIndex() + 1);

			this.listView.getItems().add(addIndex, input);
			this.listView.getSelectionModel().select(addIndex);
		}
	}

	/**
	 * Apply action handler.
	 *
	 * @param evt The action event.
	 */
	public void onApplyAction(ActionEvent evt) {
		int applyIndex = this.listView.getSelectionModel().getSelectedIndex();

		if (applyIndex >= 0) {
			T input = getInput();

			if (input != null) {
				this.listView.getItems().set(applyIndex, input);
			}
		}
	}

	/**
	 * Delete action handler.
	 *
	 * @param evt The action event.
	 */
	public void onDeleteAction(ActionEvent evt) {
		int deleteIndex = this.listView.getSelectionModel().getSelectedIndex();

		if (deleteIndex >= 0) {
			this.listView.getItems().remove(deleteIndex);
		}
	}

	/**
	 * Move up action handler.
	 *
	 * @param evt The action event.
	 */
	public void onMoveUpAction(ActionEvent evt) {
		int moveFromIndex = this.listView.getSelectionModel().getSelectedIndex();

		if (moveFromIndex > 0) {
			ObservableList<T> items = this.listView.getItems();
			T item = items.get(moveFromIndex);
			int moveToIndex = moveFromIndex - 1;

			items.set(moveFromIndex, items.get(moveToIndex));
			items.set(moveToIndex, item);
			this.listView.getSelectionModel().select(moveToIndex);
		}
	}

	/**
	 * Move down action handler.
	 *
	 * @param evt The action event.
	 */
	public void onMoveDownAction(ActionEvent evt) {
		int moveFromIndex = this.listView.getSelectionModel().getSelectedIndex();
		int moveToIndex = moveFromIndex + 1;
		ObservableList<T> items = this.listView.getItems();

		if (moveToIndex < items.size()) {
			T item = items.get(moveFromIndex);

			items.set(moveFromIndex, items.get(moveToIndex));
			items.set(moveToIndex, item);
			this.listView.getSelectionModel().select(moveToIndex);
		}
	}

}
