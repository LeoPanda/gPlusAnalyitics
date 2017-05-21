package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateServiceAsync;


import com.google.gwt.core.shared.GWT;
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
  // RPC 非同期通信用のインターフェース
  private GoogleGateServiceAsync googleAsync = GWT.create(GoogleGateService.class);
  // イベントリスナー
  private RpcGateListener<R> listener;

  /**
   * コンストラクタ
   * 
   * @param function
   *          呼び出すRPC機能
   * @param userId
   *          Google ユーザーID
   * @param listener
   *          完了通知処理記述用リスナー
   */
  public RpcGate(CallFunction function, RpcGateListener<R> listener) {
    this.function = function;
    this.listener = listener;
  }

  /**
   * RPC呼び出し要求
   */
  @SuppressWarnings("unchecked")
  public void request() {
    GenAsync<R> genAsync = new GenAsync<R>();
    genAsync.addListener((RpcGateListener<R>) this.listener);
    switch (function) {
    case GET_STOREDITEMS: {
      googleAsync.getItems((AsyncCallback<StoredItems>) genAsync.callbackR);
    }
      break;
    case INITIAL_ITEMSTORE: {
      googleAsync.initialLoadToStore((AsyncCallback<String>) genAsync.callbackR);

    }
      break;
    case UPDATE＿ITEMSTORE: {
      googleAsync.updateDataStore((AsyncCallback<String>) genAsync.callbackR);

    }
      break;
    case UPDATE_ACTIVITIES: {
      googleAsync.updateActivities((AsyncCallback<String>) genAsync.callbackR);
    }
      break;
    case CLEAR＿ITEMSTORE: {
      googleAsync.clearDataStore((AsyncCallback<String>) genAsync.callbackR);
    }
      break;

    default:
      break;
    }
  }

}
