package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.ChildCardsProcesser;
import jp.leopanda.gPlusAnalytics.interFace.CheckBoxListener;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;

/**
 * フィルターログ表示パネル
 * 
 * @author LeoPanda
 *
 */
public class FilterLogPanel extends HorizontalPanel {
  private CheckBoxListener checkListener;   //各カードのチェックボックス状態変更リスナー 
  private FilterRequestListener resetButtonListener; //リセットボタンリスナー
  private Button resetButton;
  private Label titleLabel = new Label("フィルター:");

  /**
   * コンストラクタ
   */
  public FilterLogPanel() {
    resetButton = getResetButton();
    this.add(resetButton);
    this.add(titleLabel);
    resetButton.setVisible(false);
    titleLabel.setVisible(false);
    titleLabel.addStyleName(MyStyle.FILTER_LABEL.getStyle());
  }
  /**
   * リセットボタンの生成
   * 
   * @return
   */
  public Button getResetButton() {
    Button resetButton = new Button("X");
    resetButton.addStyleName(MyStyle.FILTER_BUTTON.getStyle());
    resetButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        resetButtonListener.request(FilterType.RESET_ITEMS, null);
      }
    });
    return resetButton;
  }
  /**
   * リセットボタンのリスナーを設定する
   * @param listener
   */
  public void addRestButtonListener(FilterRequestListener listener){
    this.resetButtonListener = listener;
  }
  /**
   * 表示されたログカードにチェックボックスの動作リスナーを追加する
   * 
   * @param listener
   */
  public void addCardCheckBoxListerer(CheckBoxListener listener) {
    this.checkListener = listener;
    new ChildCardsProcesser(this) {
      @Override
      public void cardProcess(FilterLogCard card) {
        card.addCheckBoxListener(getCheckBoxListener());
      }
    }.processAll();
  }

  /**
   * カードに追加するチェックボックスリスナーを生成する
   * 
   * @return
   */
  private CheckBoxListener getCheckBoxListener() {
    return new CheckBoxListener() {
      @Override
      public void onValueChange(boolean value) {
        checkListener.onValueChange(value);
      }
    };
  }

  /**
   * パネルにログカードを追加する
   * 
   * @param widget
   */
  @Override
  public void add(Widget widget) {
    resetButton.setVisible(true);
    titleLabel.setVisible(true);
    super.add(widget);
  }

  /**
   * パネルのログカードをすべて消去する
   */
  @Override
  public void clear() {
    super.clear();
    this.add(resetButton);
    this.add(titleLabel);
    resetButton.setVisible(false);
    titleLabel.setVisible(false);
  }

}
