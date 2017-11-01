package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
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
  private GoogleGateServiceAsync googleAsync = GWT.create(GoogleGateService.class);
  private RpcGateListener<R> listener;

  /**
   * コンストラクタ
   * 
   * @param function
   * @param userId
   * @param listener
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
        googleAsync.getItems((AsyncCallback<SourceItems>) genAsync.callbackR);
      }
        break;
        
      case UPDATE＿ITEMSTORE: {
        googleAsync.updateDataStore((AsyncCallback<String>) genAsync.callbackR);
      }
        break;
        
      case CLEAR_SERVERMEMORY: {
        googleAsync.clearItemsOnMemory((AsyncCallback<String>) genAsync.callbackR);
      }
        break;

      default:
        break;
    }
  }

}
