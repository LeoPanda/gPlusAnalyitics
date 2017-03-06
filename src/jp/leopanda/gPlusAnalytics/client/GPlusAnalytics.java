package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.panel.MenuPanel;
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
    @SuppressWarnings("unused")
    Auth auth = new Auth() {

      @Override
      public void onGetToken() {
        outerPanel = new HorizontalPanel();
        outerPanel.add(new Label("データロード中..."));
        RootPanel.get(outerPanelName).add(outerPanel);
        new RpcGate<StoredItems>(CallFunction.GET_ITEM, new AfterDataLoad()) {
        }.request();
      }
    };
  }

  /**
   * データロード後の処理
   * 
   * @author LeoPanda
   *
   */
  private class AfterDataLoad implements RpcGateListener<StoredItems> {
    @Override
    public void onCallback(StoredItems result) {
      outerPanel.clear();
      outerPanel.add(new MenuPanel(result.activities, result.plusOners));
    }

  }
}
