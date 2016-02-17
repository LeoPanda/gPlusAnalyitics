package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.GoogleGate;
import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateListener;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Google+アクテビティ一覧を表示するパネルを作成するクラス
 * 
 * @author LeoPanda
 *
 */
public class PlusOnersListPane extends VerticalPanel {
	/**
	 * コンストラクタ
	 * 
	 * @param userId
	 *            表示するアクテビティのオーナーのユーザーID
	 */
	public PlusOnersListPane(String userId) {
		this.add(new Label("データ取得中..."));
		if(Global.getPlusOners() == null){
			requestItems(userId);			
		} else {
			addListTable(Global.getPlusOners());
		}
	}
	/**
	 * Google＋　activityリストのデータ取得をRPCで要求する。
	 * 
	 * @param userId
	 */
	private void requestItems(String userId) {
		new GoogleGate<PlusPeople>(
				CallFunction.getPlusOners, userId,
				new GoogleGateListener<PlusPeople>() {

					@Override
					public void onCallback(List<PlusPeople> result) {
						// 応答データのハンドリング
						Global.setPlusOners(result);
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
	private void addListTable(List<PlusPeople> items) {
		this.clear();
		this.add(new ItemListPanel<PlusPeople, PlusOnersListTable>(items,
				"+1ユーザー一覧", 10, new PlusOnersListTable(items)) {
		});
	}

}
