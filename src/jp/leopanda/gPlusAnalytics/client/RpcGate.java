package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateServiceAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GWT RPCを通じてGoogle REST APIへ非同期アクセスするためのメソッドとリスナーを提供する。
 * 
 * @author LeoPanda
 *
 */
public class RpcGate<R> {
	CallFunction function; // 呼び出す機能の種類
	R resultClass; // 戻り値のクラス
	// メソッド用パラメータ
	private String userId;
	// RPC 非同期通信用のインターフェース
	private GoogleGateServiceAsync googleAsync = GWT
			.create(GoogleGateService.class);
	// イベントリスナー
	private RpcGateListener<R> listener;
	/*
	 * コンストラクタ
	 */
	public RpcGate(CallFunction function, String userId,
			RpcGateListener<R> listener) {
		this.function = function;
		this.listener = listener;
		this.userId = userId;
	}

	/*
	 * PRC API 呼び出し要求
	 */
	@SuppressWarnings("unchecked")
	public void request() {
		GenAsync<R> genAsync = new GenAsync<R>();
		genAsync.addListener((RpcGateListener<R>) this.listener);
		switch (function) {
			case GET_ITEM : {
				googleAsync.getItems(userId,
						(AsyncCallback<ResultPack>) genAsync.callback_);
			}
				break;
			case INITIAL_LOAD : {
				googleAsync.initialLoadToStore(userId, Global.getAuthToken(),
						(AsyncCallback<String>) genAsync.callback_);

			}
				break;
			case UPDATE : {
				googleAsync.updateDataStore(userId, Global.getAuthToken(),
						(AsyncCallback<String>) genAsync.callback_);

			}
				break;
			case CLEAR : {
				googleAsync.clearDataStore(userId,
						(AsyncCallback<String>) genAsync.callback_);

			}
				break;

			default :
				break;
		}
	}
	/*
	 * googleAsync 戻り値の総称化クラス
	 */
	private class GenAsync<R_> {
		AsyncCallback<R_> callback_;
		RpcGateListener<R_> listener_;
		public void addListener(RpcGateListener<R_> listener) {
			this.listener_ = listener;
		}
		public GenAsync() {
			callback_ = new AsyncCallback<R_>() {
				@Override
				// Callback取得成功
				public void onSuccess(R_ result) {
					listener_.onCallback(result);
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
			Window.alert("RPCエラー:" + ((HostGateException) caught).getStatus());
		} else {
			Window.alert("RPCエラー:" + caught.toString());
		}
	}
}
