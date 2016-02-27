package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アイテムリストテーブルにフィルター機能を追加する
 * 
 * @author LeoPanda
 *
 */
public abstract class FilterableItemListPanel<I extends PlusItem, T extends SimpleCellTable<I>>
		extends
			ItemListPanel<I, T> {
	TextBox filterInput = new TextBox();
	/**
	 * コンストラクタ
	 * 
	 * @param itemList
	 * @param titleName
	 * @param pageSize
	 * @param itemTable
	 */
	public FilterableItemListPanel(List<I> itemList, String titleName,
			int pageSize, T itemTable) {
		super(itemList, titleName, pageSize, itemTable);
		addChangeHandler();
		spaceOfPageControl.add(filterInput);
	}
	/**
	 * 入力フィールドのクリア
	 */
	public void clearFilterInput() {
		filterInput.setText("");
	}
	/**
	 * 入力フィールドにenter key検知ハンドラを設定する
	 */
	private void addChangeHandler() {
		filterInput.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doFilter(filterInput.getValue());;
				}
			}
		});
	}
	/**
	 * フィルター実行
	 * 
	 * @param comparator
	 */
	private void doFilter(String comparator) {
		new ItemFilter<I, String>(itemList) {
			@Override
			public boolean compare(I item, String comparator) {
				return (item.getFilterSourceValue().toLowerCase()
						.contains(comparator.toLowerCase()));
			}
		}.doFilter(comparator, itemTable);
		setDisplayCounter();
		pageStart = 0;
	}
}