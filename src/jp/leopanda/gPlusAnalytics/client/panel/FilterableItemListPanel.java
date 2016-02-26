package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アイテムリストテーブルにフィルター機能を追加する
 * 
 * @author LeoPanda
 *
 */
public abstract class FilterableItemListPanel<I extends PlusItem, T extends SimpleListTable<I>>
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
		centerSpaceOfPageControl.add(filterInput);
	}

	/**
	 * 入力フィールドにチェンジハンドラを設定する
	 */
	private void addChangeHandler() {
		filterInput.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				doFilter(filterInput.getValue());;
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
		pageStart = 0;
	}
}