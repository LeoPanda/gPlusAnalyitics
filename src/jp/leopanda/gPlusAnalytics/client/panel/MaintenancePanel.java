package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.DataStoreMentenance;
import jp.leopanda.gPlusAnalytics.interFace.DataMentenanceListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * データストアのメンテナンス指示を行う画面
 * 
 * @author LeoPanda
 *
 */
public class MaintenancePanel extends VerticalPanel {

	Button updateButton = new Button("データストア更新");
	Button clearDataButton = new Button("データクリア");
	Button forceUpdateButton = new Button("強制更新");
	VerticalPanel buttonPanel;
	DataStoreMentenance mentenancer;

	/**
	 * クリック時呼び出しファンクション
	 * 
	 * @author LeoPanda
	 *
	 */
	private enum Clicked {
		UPDATE("データストアを更新します。"), CLEAR("データストアを消去します。"), FORCE_UPDATE(
				"データストアを強制更新します。");

		public String msg;

		Clicked(String msg) {
			this.msg = msg;
		}
	}
	/**
	 * コンストラクタ
	 */
	public MaintenancePanel() {
		if (mentenancer == null) {
			mentenancer = new DataStoreMentenance(new Callback());
		}
		this.add(getButtonPanel());
	}
	/**
	 * ボタン表示パネルの作成
	 * 
	 * @return
	 */
	private VerticalPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new VerticalPanel();
			buttonPanel.add(updateButton);
			buttonPanel.add(new HTML("<br/>"));
			buttonPanel.add(forceUpdateButton);
			buttonPanel.add(new HTML("<br/>"));
			buttonPanel.add(clearDataButton);
			addClickHandlers();
		}
		return buttonPanel;
	}
	/**
	 * ボタンクリックハンドラのセット
	 */
	private void addClickHandlers() {
		updateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confirmClicked(Clicked.UPDATE);
			}
		});
		clearDataButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confirmClicked(Clicked.CLEAR);
			}
		});
		forceUpdateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confirmClicked(Clicked.FORCE_UPDATE);
			}
		});
	}
	/**
	 * クリックされたボタンを確認する
	 * 
	 * @param clicked
	 */
	private void confirmClicked(Clicked clicked) {
		if (Window.confirm(clicked.msg + "よろしいですか？")) {
			setPanelToOngoing();
			goClickedFunction(clicked);
		}
	}
	/**
	 * クリックされた指示を実行する
	 * 
	 * @param clicked
	 */
	private void goClickedFunction(Clicked clicked) {
		switch (clicked) {
			case UPDATE :
				mentenancer.updateDataStore(false);
				break;
			case FORCE_UPDATE :
				mentenancer.updateDataStore(true);
				break;
			case CLEAR :
				mentenancer.clearDataStore();
				break;
			default :
				break;
		}
	}
	/**
	 * 　パネルをデータ更新中に変更
	 */
	private void setPanelToOngoing() {
		this.clear();
		this.add(new Label("データストア更新中・・・"));
	}
	/**
	 * RPC実行後のコールバックを受信するクラス
	 * 
	 * @author LeoPanda
	 *
	 */
	private class Callback implements DataMentenanceListener {
		@Override
		public void onCallback(String result) {
			Window.alert("更新指示が出されました。データストアの更新が完了するにはしばらく時間がかかる場合があります。ブラウザをリロードすると更新が反映されます。");
			MaintenancePanel.this.clear();
			MaintenancePanel.this.add(buttonPanel);
		}
	}
}
