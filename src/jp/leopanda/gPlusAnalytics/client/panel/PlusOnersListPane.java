package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

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
			this.add(new Label("データストアが初期化されていません。メンテナンスメニューからデータストアの初期化を実行してください。"));		
		} else {
			addListTable(Global.getPlusOners());
		}
	}
	/**
	 * 一覧テーブルを追加する
	 * 
	 * @param imtes
	 */
	private void addListTable(List<PlusPeople> items) {
		this.clear();
		this.add(new FilterableItemListPanel<PlusPeople, PlusOnersListTable>(items,
				"+1ユーザー一覧", 10, new PlusOnersListTable(items)) {
		});
	}

}
