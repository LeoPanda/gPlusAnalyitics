package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * HTMLを表示するテーブルカラム
 * @author LeoPanda
 *
 */
public abstract class ButtonColumn<T> extends Column<T,String> {
	static final ButtonCell cell = new ButtonCell();
	public ButtonColumn() {
		super(cell);
		this.setFieldUpdater(new FieldUpdater<T, String>() {
			@Override
			public void update(int index, T object, String value) {
				addClickEvent(index,object);
			}

		});
	}
	 /**
	  * ボタンクリック時の処理
	  * @param index
	  * @param object
	  * @param value
	  */
	abstract public void addClickEvent(int index, T item);

}
