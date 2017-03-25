package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.RpcGate;
import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;


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
  Button initialButton = new Button("データストア初期ロード");
  VerticalPanel buttonPanel;

  /**
   * コンストラクタ
   */
  public MaintenancePanel() {
    this.add(getButtonPanel());
  }

  /*
   * ボタン表示パネルの作成
   */
  private VerticalPanel getButtonPanel() {
    if (buttonPanel == null) {
      buttonPanel = new VerticalPanel();
      buttonPanel.add(updateButton);
      buttonPanel.add(new HTML(FixedString.BLANK_CELL.getValue()));
      buttonPanel.add(initialButton);
      buttonPanel.add(new HTML(FixedString.BLANK_CELL.getValue()));
      buttonPanel.add(clearDataButton);
      addClickHandlers();
    }
    return buttonPanel;
  }

  /*
   * ボタンクリックハンドラのセット
   */
  private void addClickHandlers() {
    updateButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        confirmClicked(CallFunction.UPDATE＿ITEMSTORE);
      }
    });
    clearDataButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        confirmClicked(CallFunction.CLEAR＿ITEMSTORE);
      }
    });
    initialButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        confirmClicked(CallFunction.INITIAL_ITEMSTORE);
      }
    });
  }

  /*
   * クリックされたボタンを確認する
   */
  private void confirmClicked(CallFunction clicked) {
    if (Window.confirm(clicked.msg + "よろしいですか？")) {
      setPanelToOnGoing();
      new RpcGate<String>(clicked, new Callback()) {}.request();
    }
  }

  /*
   * 　パネルをデータ更新中に変更
   */
  private void setPanelToOnGoing() {
    this.clear();
    this.add(new Label("データストア更新中・・・"));
  }

  /*
   * RPC実行後のコールバックを受信するクラス
   */
  private class Callback implements RpcGateListener<String> {
    @Override
    public void onCallback(String result) {
      Window.alert("データストアの更新が完了するまでもうしばらく時間がかかる場合があります。更新データを表示するにはブラウザをリロードしてください。");
      MaintenancePanel.this.clear();
      MaintenancePanel.this.add(buttonPanel);
    }
  }
}
