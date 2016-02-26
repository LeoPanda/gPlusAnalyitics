package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * セルテーブルの表示データをフィルタリングするための抽象クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemFilter<I extends PlusItem, C> {
	List<I> originalItems; // フィルター実行前のオリジナルデータ
	/**
	 * コンストラクタ
	 * @param originalItems
	 */
	public ItemFilter(List<I> originalItems) {
		this.originalItems = originalItems;
	}
	/**
	 * フィルタを実行してセルテーブルの表示データを変更する
	 * 
	 * @param comparator
	 *            　フィルタ比較オブジェクト
	 * @param itemTable
	 *            	表示データを変更したいセルテーブル
	 */
	public void doFilter(C comparator, SimpleListTable<I> itemTable ) {
		List<I> displayList = itemTable.getDisplayList();
		displayList.clear();
		for (I item : originalItems) {
			if (comparator == null) {
				displayList.add(item);
			} else if (compare(item, comparator)) {
				displayList.add(item);
			}
		}
		itemTable.setPageStart(0);
	}
	/**
	 * フィルタリング方法を記述する
	 * 
	 * @param item
	 * @param comparator
	 * @return
	 */
	abstract public boolean compare(I item, C comparator);
}
