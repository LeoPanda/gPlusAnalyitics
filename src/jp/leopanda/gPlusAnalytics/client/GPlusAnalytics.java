package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.common.client.GoogleLoginBar;
import jp.leopanda.common.client.LoginBarListener;
import jp.leopanda.common.client.GoogleLoginBar.InfoEvent;
import jp.leopanda.common.client.GoogleLoginBar.ScopeName;
import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.panel.MenuPanel;
import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;

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
  private GoogleLoginBar loginBar; // Google認証用バー
  private final String googleLoginBarName = "loginBarContainer"; // HTMLのdiv名に合わせる
  private final String outerPanelName = "outerPanel"; // HTMLのdiv名に合わせる

  /**
   * メイン処理
   */
  public void onModuleLoad() {
    // Googleログインバーの表示
    loginBar =
        new GoogleLoginBar(Global.getApiClientid(), GoogleLoginBar.addScope(ScopeName.PLUSME)
            + GoogleLoginBar.addScope(ScopeName.PLUSLOGIN));
    loginBar.addListerner(new AfterLogin());
    RootPanel.get(googleLoginBarName).add(loginBar);
  }

  /**
   * Googleログイン後の処理
   */
  private class AfterLogin implements LoginBarListener {
    @Override
    // ログオフされた
    public void onLoggedOff(InfoEvent event) {
      outerPanel.removeFromParent();
    }

    @Override
    // ログインを検出後、データストア更新処理を呼び出す
    public void onLoggedIn(InfoEvent event) {
      Global.setAuthToken(loginBar.getToken());
      Global.setgoogeUserId(loginBar.getGoogleInnerId());
      outerPanel = new HorizontalPanel();
      outerPanel.add(new Label("データロード中..."));
      RootPanel.get(outerPanelName).add(outerPanel);
      new RpcGate<ResultPack>(CallFunction.GET_ITEM, Global.getGoogleUserId(), 
          new AfterDataLoad()) {}
          .request();
    }
  }
  
  /**
   * データロード後の処理
   * 
   * @author LeoPanda
   *
   */
  
  private class AfterDataLoad implements RpcGateListener<ResultPack> {
    @Override
    public void onCallback(ResultPack result) {
      Global.setActivityItems(result.activities);
      Global.setPlusOners(result.plusOners);
      outerPanel.clear();
      outerPanel.add(new MenuPanel());
    }

  }
}
