package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.googleAuthorization.client.ForbiddenException;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateServiceAsync;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

import java.io.IOException;

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

  /*
   * googleAsync 戻り値の総称化クラス
   */
  private class GenAsync<R1> {
    AsyncCallback<R1> callbackR;
    RpcGateListener<R1> listenerR;

    public void addListener(RpcGateListener<R1> listener) {
      this.listenerR = listener;
    }

    public GenAsync() {
      callbackR = new AsyncCallback<R1>() {
        @Override
        // Callback取得成功
        public void onSuccess(R1 result) {
          listenerR.onCallback(result);
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
   * エラー時の処理ハンドラ
   * 
   * @param caught
   *          スローオブジェクト
   */
  private void asyncErrorHandler(Throwable caught) {
    if (caught instanceof HostGateException) {
      Window.alert("RPCエラー:" + ((HostGateException) caught).getStatus());
    } else if (caught instanceof IOException) {
      Window.alert("RPC IOエラー:" + (caught.getMessage()));
    } else if (caught instanceof ForbiddenException) {
      if (Window.confirm("アクセス権が不足しています。リロードして認証を取得し直しますか？")) {
        Window.Location.reload();
      }
    } else {
      Window.alert("RPCエラー:" + caught.toString());
    }
  }
}
