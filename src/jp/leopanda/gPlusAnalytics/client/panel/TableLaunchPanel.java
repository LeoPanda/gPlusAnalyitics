package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.ItemEventListener;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 一覧表表示パネル
 * 
 * @author LeoPanda
 *
 */
public class TableLaunchPanel extends HorizontalPanel {
	ActivityTable activityTable;
	PlusOnersTable plusOnersTable;
	FilterableActivityListPanel activityTablePanel;
	FilterableItemListPanel<PlusPeople, PlusOnersTable> plusOnersTablePanel;
	/**
	 * コンストラクタ
	 */
	public TableLaunchPanel() {
		if ((Global.getActivityItems() == null)
				|| (Global.getPlusOners() == null)) {
			this.add(new Label("メンテナンスニューからデータストアの初期ロードを行ってください。"));
		} else {
			setUpTables();
			this.add(plusOnersTablePanel);
			this.add(new HTML("<br/>&nbsp;&nbsp;&nbsp;<br/>"));
			this.add(activityTablePanel);
			addPlusOnerFilterButtonHandler();
		}
	}
	/**
	 * 一覧表テーブルと表示パネルを新規作成する
	 */
	private void setUpTables() {
		activityTable = new ActivityTable(Global.getActivityItems());
		plusOnersTable = new PlusOnersTable(Global.getPlusOners());
		activityTablePanel = new FilterableActivityListPanel(
				Global.getActivityItems(), "アクティビティ一覧", 7, activityTable);
		plusOnersTablePanel = new FilterableItemListPanel<PlusPeople, PlusOnersTable>(
				Global.getPlusOners(), "+1ユーザー一覧", 10, plusOnersTable) {
		};

	}
	/**
	 * +1er一覧でアクティビティフィルタボタンが押された場合の処理ハンドラ
	 */
	private void addPlusOnerFilterButtonHandler() {
		plusOnersTable
				.addFilterButtonClickEventListener(new ItemEventListener<PlusPeople>() {
					@Override
					public void onEvent(PlusPeople item) {
						activityTablePanel.doPlusOnerFilter(item.getId(),
								item.getDisplayName());
					}
				});
	}
}
