package msjfxuicomponents;

import java.util.Collection;
import java.util.function.Function;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;

public class AutoCompleteComboBox<T> extends ComboBox<T> {
	private MSComboBoxListViewSkin<T> skin = null;
	private Function<String, Collection<T>> entitiesLoader = null;
	private Double popupMaxHeight = 200.0;
	private Double rowHeight = 25.0;

	public AutoCompleteComboBox() {
		this.initListeners();
	}

	private void performSearch() throws NullPointerException {
		String value = this.getEditor().getText();
		boolean selectedWasElementEntity = this.isSelectedElementEntity();

		if (value == null)
			value = "";

		Collection<T> result = this.getEntitiesLoader().apply(value);

		this.getItems().clear();
		this.getItems().addAll(result);

		if (selectedWasElementEntity) {
			this.getEditor().setText(value);
			this.getEditor().positionCaret((getEditor().getText().length()));
		}

		this.skin.msResize(this.getItems().size(), this.getRowHeight(), this.getPopupMaxHeight());
	}

	private void initListeners() {
		this.setEditable(true);
		this.getEditor().setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
					|| (event.getCode() == KeyCode.DOWN && this.isShowing()) || event.getCode() == KeyCode.UP
					|| event.getCode() == KeyCode.ENTER || event.getCode().equals(KeyCode.SHIFT)
					|| event.getCode() == KeyCode.CONTROL || event.isControlDown() || event.getCode() == KeyCode.HOME
					|| event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
				return;
			}

			performSearch();
		});
		this.getEditor().setOnMouseClicked(event -> {
			show();
		});
		this.getEditor().focusedProperty().addListener(event -> {
			if (getSelectionModel().getSelectedIndex() < 0) {
				getEditor().setText(null);

				performSearch();
			} else if (getItems().get(getSelectionModel().getSelectedIndex()).toString()
					.equals(getEditor().getText())) {
				getEditor().setText(null);
			}
		});
	}

	public boolean isSelectedElementEntity() {
		return this.getSelectionModel().getSelectedIndex() >= 0;
	}

	public T getEntity() {
		if (this.getSelectionModel().getSelectedIndex() < 0)
			return null;
		else
			try {
				return this.getItems().get(this.getSelectionModel().getSelectedIndex());
			} catch (IndexOutOfBoundsException exp) {
				return null;
			}
	}

	public void setEntity(T value) {
		if (value == null)
			this.getSelectionModel().select(0);
		else
			this.getSelectionModel().select(this.getItems().indexOf(value));
	}

	public Function<String, Collection<T>> getEntitiesLoader() {
		return this.entitiesLoader;
	}

	public void setEntitiesLoader(Function<String, Collection<T>> entitiesLoader) {
		this.entitiesLoader = entitiesLoader;
	}

	public Double getPopupMaxHeight() {
		return this.popupMaxHeight;
	}

	public void setPopupMaxHeight(Double popupMaxHeight) {
		this.popupMaxHeight = popupMaxHeight;
	}

	public Double getRowHeight() {
		return this.rowHeight;
	}

	public void setRowHeight(Double rowHeight) {
		this.rowHeight = rowHeight;
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		this.skin = new MSComboBoxListViewSkin<>(this);

		return this.skin;
	}

	public class MSComboBoxListViewSkin<S> extends ComboBoxListViewSkin<S> {
		public MSComboBoxListViewSkin(final ComboBox<S> comboBox) {
			super(comboBox);
		}

		public void msResize(int rows, double rowHeight, double maxHeight) {
			double height = Math.min(rows * rowHeight, maxHeight);

			this.getListView().setMinHeight(height);
			this.getListView().setMaxHeight(height);
			this.getListView().setPrefHeight(height);
			this.getListView().resize(this.getListView().getWidth(), height);

			double oldHeight = getHeight();

			setHeight(0);
			setHeight(oldHeight);
		}
	}
}
