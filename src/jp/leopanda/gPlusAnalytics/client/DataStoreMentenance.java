package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.interFace.DataMentenanceListener;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateServiceAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * データストアメンテナンスのためのメソッドを提供する
 * 
 * @author LeoPanda
 *
 */
public class DataStoreMentenance {
	// Google API 非同期通信用のインターフェース
	private GoogleGateServiceAsync googleAsync = GWT
			.create(GoogleGateService.class);
	// リスナー
	private DataMentenanceListener listener;
	/**
	 * コンストラクタ
	 */
	public DataStoreMentenance(DataMentenanceListener listener) {
		this.listener = listener;
	}
	/**
	 * データストアをクリアする。
	 */
	public void clearDataStore() {
		googleAsync.clearDataStore(Global.getGoogleUserId(), new Callback());
	}
	/**
	 * データストアのアップデート
	 * 
	 * @param foceUpdate
	 *            trueで強制更新
	 */
	public void updateDataStore(boolean forceUpdate) {
		googleAsync.updateDataStore(Global.getGoogleUserId(),
				Global.getAuthToken(), forceUpdate, new Callback());
	}
	/**
	 * コールバック受信クラス
	 * 
	 * @author LeoPanda
	 *
	 */
	private class Callback implements AsyncCallback<String> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("データストア更新に失敗しました。ログを確認してください。");
		}
		@Override
		public void onSuccess(String result) {
			listener.onCallback(result);
		}
	}
}
