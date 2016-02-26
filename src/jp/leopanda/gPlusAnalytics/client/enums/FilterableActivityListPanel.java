/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.enums;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.panel.ActivityListTable;
import jp.leopanda.gPlusAnalytics.client.panel.FilterableItemListPanel;
import jp.leopanda.gPlusAnalytics.client.panel.ItemFilter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * ユーザ別＋１フィルター機能付きアクテビティ一覧
 * 
 * @author LeoPanda
 *
 */
public class FilterableActivityListPanel
		extends
			FilterableItemListPanel<PlusActivity, ActivityListTable> {

	/**
	 * コンストラクタ
	 * 
	 * @param items
	 * @param pageSize
	 */
	public FilterableActivityListPanel(List<PlusActivity> items, int pageSize) {
		super(items, "アクテビティ一覧", pageSize, new ActivityListTable(items));
	}
	/**
	 * +1erでフィルター
	 * @param plusOnerId
	 */
	public void doPlusOnerFilter(String plusOnerId) {
		new ItemFilter<PlusActivity, String>(itemList) {
			@Override
			public boolean compare(PlusActivity item, String comparator) {
				return item.getPlusOnerIds().contains(comparator);
			}
		}.doFilter(plusOnerId, itemTable);
		pageStart = 0;
	}

}
