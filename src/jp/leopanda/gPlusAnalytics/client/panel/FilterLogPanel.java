package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.ChildCardsProcesser;
import jp.leopanda.gPlusAnalytics.interFace.CheckBoxListener;

/**
 * フィルターログ表示パネル
 * 
 * @author LeoPanda
 *
 */
public class FilterLogPanel extends HorizontalPanel {
  private CheckBoxListener checkListener;
  private Label titleLabel = new Label("フィルター:");

  /**
   * コンストラクタ
   */
  public FilterLogPanel() {
    this.add(titleLabel);
    titleLabel.setVisible(false);
    titleLabel.addStyleName(MyStyle.FILTER_LABEL.getStyle());
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
    titleLabel.setVisible(true);
    super.add(widget);
  }

  /**
   * パネルのログカードをすべて消去する
   */
  @Override
  public void clear() {
    super.clear();
    this.add(titleLabel);
  }

}
