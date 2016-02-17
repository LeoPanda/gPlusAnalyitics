package jp.leopanda.gPlusAnalytics.client;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateListener;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateServiceAsync;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GWT RPCを通じてGoogle REST APIへ非同期アクセスするためのメソッドとリスナーを提供する。
 * 
 * @author LeoPanda
 *
 */
public class GoogleGate<T1 extends PlusItem> {
	CallFunction function; // 呼び出す機能の種類
	T1 targetClass; // 戻り値のクラス
	// メソッド用パラメータ
	private String userId;
	/*
	 * コンストラクタ
	 */
	public GoogleGate(CallFunction function, String userId,
			GoogleGateListener<T1> listener) {
		this.function = function;
		this.listener = listener;
		this.userId = userId;
	}
	// Google API 非同期通信用のインターフェース
	private GoogleGateServiceAsync googleAsync = GWT
			.create(GoogleGateService.class);
	// イベントリスナー
	private GoogleGateListener<T1> listener;
	/*
	 * Google+ API 呼び出し要求
	 */
	@SuppressWarnings("unchecked")
	public void requestRPC() {
		switch (function) {
			case getActivities : {
				GenAsync<PlusActivity> genAsync = new GenAsync<PlusActivity>();
				genAsync.addListener((GoogleGateListener<PlusActivity>) this.listener);
				googleAsync.getActivities(userId, Global.getAuthToken(),
						(AsyncCallback<List<PlusActivity>>) genAsync.callback_);
			}
				break;

			case getPlusOners : {
				GenAsync<PlusPeople> genAsync = new GenAsync<PlusPeople>();
				genAsync.addListener((GoogleGateListener<PlusPeople>) this.listener);
				googleAsync.getPlusOners(userId, Global.getAuthToken(),
						(AsyncCallback<List<PlusPeople>>) genAsync.callback_);

			}
				break;
			default :
				break;
		}
	}
	/*
	 * googleAsync 戻り値の総称化クラス
	 */
	private class GenAsync<T2> {
		AsyncCallback<List<T2>> callback_;
		GoogleGateListener<T2> listener_;
		public void addListener(GoogleGateListener<T2> listener) {
			this.listener_ = listener;
		}
		public GenAsync() {
			callback_ = new AsyncCallback<List<T2>>() {
				@Override
				// Callback取得成功
				public void onSuccess(List<T2> result) {
					if (result == null) {
						Window.alert("データストアが作成されていません。メンテナンスメニューからデータストア更新を実行してください。");
					} else {
						listener_.onCallback(result);
					}
				}
				@Override
				// 取得失敗
				public void onFailure(Throwable caught) {
					asyncErrorHandler(caught);
				}
			};
		}
	}
	/**
	 * 取得エラー時処理
	 * 
	 * @param caught
	 */
	private void asyncErrorHandler(Throwable caught) {
		if (caught instanceof HostGateException) {
			Window.alert("google+ REST API 同期に失敗しました。HTTP StatisCode="
					+ ((HostGateException) caught).getStatus());
		} else {
			Window.alert("google+ REST API 同期時にRPCエラー:" + caught.toString());
		}
	}

}
