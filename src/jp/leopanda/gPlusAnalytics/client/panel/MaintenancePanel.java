package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.RpcGate;
import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;

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
  Button clearMemoryButton = new Button("サーバーメモリーのクリア");
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
      buttonPanel.add(clearMemoryButton);
      buttonPanel.add(new HTML(FixedString.BLANK_CELL.getValue()));
      addClickHandlers();
    }
    return buttonPanel;
  }

  /*
   * ボタンクリックハンドラのセット
   */
  private void addClickHandlers() {
    updateButton.addClickHandler(event -> confirmClicked(CallFunction.UPDATE＿ITEMSTORE));
    clearMemoryButton.addClickHandler(event -> confirmClicked(CallFunction.CLEAR_SERVERMEMORY));
  }

  /*
   * クリックされたボタンを確認する
   */
  private void confirmClicked(CallFunction clicked) {
    if (Window.confirm(clicked.msg + "よろしいですか？")) {
      setPanelToOnGoing();
      new RpcGate<String>(clicked, result -> {
        Window.alert("更新指示が完了しました。画面をリロードします。");
        Window.Location.reload();
      }) {}.request();
    }
  }

  /*
   * パネルをデータ更新中に変更
   */
  private void setPanelToOnGoing() {
    this.clear();
    this.add(new Label("データストア更新中・・・"));
  }
}
