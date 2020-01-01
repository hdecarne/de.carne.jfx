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
package de.carne.jfx.scene.control;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.util.Late;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * Utility class for editing {@link ListView} elements.
 *
 * @param <T> The list element's type.
 */
public abstract class ListViewEditor<T> {

	private final Late<ListView<T>> listViewParam = new Late<>();

	/**
	 * Initialize the editor.
	 *
	 * @param listView The {@link ListView} control to use for editing.
	 * @return This editor.
	 */
	public ListViewEditor<T> init(ListView<T> listView) {
		ListView<T> initializedListView = this.listViewParam.set(listView);

		initializedListView.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> setInput(n));
		return this;
	}

	/**
	 * Set a add command button.
	 *
	 * @param cmdButton The add command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setAddCommand(Button cmdButton) {
		cmdButton.setOnAction(this::onAddAction);
		return this;
	}

	/**
	 * Set a apply command button.
	 *
	 * @param cmdButton The apply command button to use.
	 * @return This editor.
	 */
	public ListViewEditor<T> setApplyCommand(Button cmdButton) {
		ListView<T> listView = getListView();

		cmdButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
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
		ListView<T> listView = getListView();

		cmdButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
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
		ListView<T> listView = getListView();

		cmdButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
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
		ListView<T> listView = getListView();

		cmdButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
		cmdButton.setOnAction((evt) -> onMoveDownAction(evt));
		return this;
	}

	/**
	 * Called during add or apply action to retrieve the current input.
	 *
	 * @return The entered list element or {@code null} if the current input is empty or not valid.
	 */
	@Nullable
	protected abstract T getInput();

	/**
	 * Called on selection change to initialize the input data with the current selection.
	 *
	 * @param input The selected list element's data or {@code null} if nothing is selected.
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
			ListView<T> listView = getListView();

			int addIndex = Math.max(0, listView.getSelectionModel().getSelectedIndex() + 1);

			listView.getItems().add(addIndex, input);
			listView.getSelectionModel().select(addIndex);
		}
	}

	/**
	 * Apply action handler.
	 *
	 * @param evt The action event.
	 */
	public void onApplyAction(ActionEvent evt) {
		ListView<T> listView = getListView();
		int applyIndex = listView.getSelectionModel().getSelectedIndex();

		if (applyIndex >= 0) {
			T input = getInput();

			if (input != null) {
				listView.getItems().set(applyIndex, input);
			}
		}
	}

	/**
	 * Delete action handler.
	 *
	 * @param evt The action event.
	 */
	public void onDeleteAction(ActionEvent evt) {
		ListView<T> listView = getListView();
		int deleteIndex = listView.getSelectionModel().getSelectedIndex();

		if (deleteIndex >= 0) {
			listView.getItems().remove(deleteIndex);
		}
	}

	/**
	 * Move up action handler.
	 *
	 * @param evt The action event.
	 */
	public void onMoveUpAction(ActionEvent evt) {
		ListView<T> listView = getListView();
		int moveFromIndex = listView.getSelectionModel().getSelectedIndex();

		if (moveFromIndex > 0) {
			ObservableList<T> items = listView.getItems();
			T item = items.get(moveFromIndex);
			int moveToIndex = moveFromIndex - 1;

			items.set(moveFromIndex, items.get(moveToIndex));
			items.set(moveToIndex, item);
			listView.getSelectionModel().select(moveToIndex);
		}
	}

	/**
	 * Move down action handler.
	 *
	 * @param evt The action event.
	 */
	public void onMoveDownAction(ActionEvent evt) {
		ListView<T> listView = getListView();
		int moveFromIndex = listView.getSelectionModel().getSelectedIndex();
		int moveToIndex = moveFromIndex + 1;
		ObservableList<T> items = listView.getItems();

		if (moveToIndex < items.size()) {
			T item = items.get(moveFromIndex);

			items.set(moveFromIndex, items.get(moveToIndex));
			items.set(moveToIndex, item);
			listView.getSelectionModel().select(moveToIndex);
		}
	}

	private ListView<T> getListView() {
		return this.listViewParam.get();
	}

}
