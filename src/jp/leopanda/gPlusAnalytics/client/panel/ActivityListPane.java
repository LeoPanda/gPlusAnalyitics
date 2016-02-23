package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Google+アクテビティ一覧を表示するパネルを作成するクラス
 * 
 * @author LeoPanda
 *
 */
public class ActivityListPane extends VerticalPanel {
	/**
	 * コンストラクタ
	 * 
	 * @param userId
	 *            表示するアクテビティのオーナーのユーザーID
	 */
	public ActivityListPane(String userId) {
		this.add(new Label("データ取得中..."));
		if(Global.getActivityItems() == null){
			this.add(new Label("データストアが初期化されていません。メンテナンスメニューからデータストアの初期化を実行してください。"));		
		} else {
			addListTable(Global.getActivityItems());
		}
	}
	/**
	 * 一覧テーブルを追加する
	 * 
	 * @param imtes
	 */
	private void addListTable(List<PlusActivity> items) {
		this.clear();
		this.add(new ItemListPanel<PlusActivity, ActivityListTable>(items,
				"アクティビティ一覧", 7, new ActivityListTable(items)) {
		});
	}

}
