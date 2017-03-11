package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.panel.MainPanel;
import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.googleAuthorization.client.Auth;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GPlusAnalytics implements EntryPoint {
  // 画面構成要素のメンバ変数
  private HorizontalPanel outerPanel; // 外枠
  private final String outerPanelName = "outerPanel"; // HTMLのdiv名に合わせる

  /**
   * メイン処理
   */
  public void onModuleLoad() {
    
    outerPanel = new HorizontalPanel();
    outerPanel.add(new Label("認証取得中..."));
    RootPanel.get(outerPanelName).add(outerPanel);

    // oAuth2認証取得リクエスト
    @SuppressWarnings("unused")
    Auth auth = new Auth() {

      @Override
      public void onGetToken() {
        // 認証取得後の処理
        requestDataLoad();
      }

    };
  }

  /**
   * サーバーサイドデータの取得をリクエストする
   */
  private void requestDataLoad() {
    outerPanel.clear();
    outerPanel.add(new Label("データロード中..."));
    new RpcGate<StoredItems>(CallFunction.GET_STOREDITEMS, new OnDataLoad()) {
    }.request();
  }

  /**
   * データロード取得後の処理
   * 
   * @author LeoPanda
   *
   */
  private class OnDataLoad implements RpcGateListener<StoredItems> {
    @Override
    public void onCallback(StoredItems result) {
      outerPanel.clear();
      outerPanel.add(new MainPanel(result.activities, result.plusOners));
    }

  }
}
