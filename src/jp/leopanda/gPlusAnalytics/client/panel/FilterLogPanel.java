package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.function.Consumer;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.NumOfPlusOneFilterKeyword;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.CheckBoxListener;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;

/**
 * フィルターログ表示パネル
 * 
 * @author LeoPanda
 *
 */
public class FilterLogPanel extends HorizontalPanel {
  private FilterRequestListener resetButtonListener; // リセットボタンリスナー
  private Button resetButton;
  private Label titleLabel = new Label("フィルター:");
  private boolean isLeadCard;

  /**
   * コンストラクタ
   */
  public FilterLogPanel() {
    resetButton = getResetButton();
    titleLabel.addStyleName(MyStyle.FILTER_LABEL.getStyle());
    initialPanel();
  }

  /**
   * ログカードをパネルに追加する
   * 
   * @param filterType
   * @param keyword
   * @param listener
   * @return
   */
  public void addFilterLogCard(FilterType filterType, Object keyword,
      CheckBoxListener listener) {
    add(getCard(filterType, keyword, listener));
    panelOperatorVisible(true);
    cardBooleansVisibler();
  }

  /**
   * ログカードを生成する
   * 
   * @param filterType
   * @param keyword
   * @param listener
   * @return
   */
  private FilterLogCard getCard(FilterType filterType, Object keyword, CheckBoxListener listener) {
    FilterLogCard card;
    if (keyword instanceof PlusActivity) {
      card = new FilterLogCard(FilterType.PLUSONER_TABLE_ACTIVITY, (PlusActivity) keyword);
    } else if (keyword instanceof PlusPeople) {
      card = new FilterLogCard(FilterType.ACTIVITY_TABLE_PLUSONER, (PlusPeople) keyword);
    } else if (keyword instanceof String) {
      card = new FilterLogCard(filterType, (String) keyword);
    } else if (keyword instanceof NumOfPlusOneFilterKeyword) {
      card = new FilterLogCard(filterType, (NumOfPlusOneFilterKeyword) keyword);
    } else {
      return null;
    }
    card.addCheckBoxListener(value -> {
      cardBooleansVisibler();
      listener.onValueChange(value);
    });
    return card;
  }

  /**
   * リセットボタンの生成
   * 
   * @return
   */
  private Button getResetButton() {
    Button resetButton = new Button("X");
    resetButton.addStyleName(MyStyle.FILTER_BUTTON.getStyle());
    resetButton.addClickHandler(event -> resetButtonListener.request(FilterType.RESET_ITEMS, null));
    return resetButton;
  }

  /**
   * リセットボタンのリスナーを設定する
   * 
   * @param listener
   */
  public void addRestButtonListener(FilterRequestListener listener) {
    this.resetButtonListener = listener;
  }

  /**
   * パネルの初期表示
   */
  private void initialPanel() {
    this.add(resetButton);
    this.add(titleLabel);
    panelOperatorVisible(false);
  }

  /**
   * パネル操作領域の表示設定
   * 
   * @param bool
   */
  private void panelOperatorVisible(boolean bool) {
    resetButton.setVisible(bool);
    titleLabel.setVisible(bool);
  }

  /**
   * 各ログカード論理和チェックの表示設定
   * 
   * @param card
   * @param logCardCounter
   */
  private void cardBooleansVisibler() {
    isLeadCard = true;
    forEachCards(card -> {
      if (isLeadCard) {
        card.setBooleansVisible(false);
        card.resetBooleans();
        isLeadCard = false;
      } else {
        card.setBooleansVisible(true);
      }
    });
  }

  /**
   * パネルのログカードをすべて消去し再表示する
   */
  @Override
  public void clear() {
    super.clear();
    initialPanel();
  }

  /**
   * すべてのログカードを処理する
   */
  public void forEachCards(Consumer<FilterLogCard> action) {
    this.forEach(widget -> {
      if (widget.getClass() == FilterLogCard.class) {
        FilterLogCard card = (FilterLogCard) widget;
        if (card.getEnableCheck()) {
          action.accept(card);
        }
      }
    });
  }
}
