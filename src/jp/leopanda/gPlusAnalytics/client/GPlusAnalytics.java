package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.common.client.GoogleLoginBar;
import jp.leopanda.common.client.LoginBarListener;
import jp.leopanda.common.client.GoogleLoginBar.InfoEvent;
import jp.leopanda.common.client.GoogleLoginBar.ScopeName;
import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.panel.MenuPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
		loginBar = new GoogleLoginBar(Global.getApiClientid(),
				GoogleLoginBar.addScope(ScopeName.PLUSME)
						+ GoogleLoginBar.addScope(ScopeName.PLUSLOGIN));
		loginBar.addListerner(new LoginControl());
		RootPanel.get(googleLoginBarName).add(loginBar);
	}
	/**
	 * Googleログインバー　イベントハンドラ
	 */
	private class LoginControl implements LoginBarListener {
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
			outerPanel.add(new MenuPanel());
			RootPanel.get(outerPanelName).add(outerPanel);

		}
	}
}