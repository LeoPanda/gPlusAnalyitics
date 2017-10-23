package jp.leopanda.gPlusAnalytics.client;

import java.io.IOException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.googleAuthorization.client.ForbiddenException;

/**
 * Async RPCの戻り値定義クラス
 * 
 * @author LeoPanda
 *
 */
public class GenAsync<R> {
  public AsyncCallback<R> callbackR;
  public RpcGateListener<R> listenerR;

  public void addListener(RpcGateListener<R> listener) {
    this.listenerR = listener;
  }

  public GenAsync() {
    callbackR = new AsyncCallback<R>() {
      @Override
      // Callback取得成功
      public void onSuccess(R result) {
        listenerR.onCallback(result);
      }

      @Override
      // 取得失敗
      public void onFailure(Throwable caught) {
        asyncErrorHandler(caught);
      }
    };
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
