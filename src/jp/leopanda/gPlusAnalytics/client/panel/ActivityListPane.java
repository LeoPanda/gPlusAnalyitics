package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.GoogleGate;
import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateListener;

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
			requestItems(userId);			
		} else {
			addListTable(Global.getActivityItems());
		}
	}
	/**
	 * Google＋　activityリストのデータ取得をRPCで要求する。
	 * 
	 * @param userId
	 */
	private void requestItems(String userId) {
		new GoogleGate<PlusActivity>(
				CallFunction.getActivities, userId,
				new GoogleGateListener<PlusActivity>() {

					@Override
					public void onCallback(List<PlusActivity> result) {
						// 応答データのハンドリング
						Global.setActivityItems(result);
						addListTable(result);
					}

				}) {

		}.requestRPC();
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
