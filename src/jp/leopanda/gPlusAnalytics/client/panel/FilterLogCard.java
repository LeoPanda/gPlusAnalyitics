package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.BOOLEANS;
import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.CheckBoxListener;
import jp.leopanda.panelFrame.filedParts.ListBoxField;

/**
 * フィルター履歴を記録するカード
 * 
 * @author LeoPanda
 */
public class FilterLogCard extends HorizontalPanel {
  private PlusActivity activity = null;
  private PlusPeople plusOner = null;
  private String keyword = null;
  private FilterType filterType = null;
  private CheckBox enableCheck = new CheckBox();
  private ListBoxField booleans;
  private CheckBoxListener checkBoxListener = null;

  /**
   * チェックボックス変更リスナーの設定
   * 
   * @param listener
   */
  public void addCheckBoxListener(CheckBoxListener listener) {
    if (this.checkBoxListener == null) {
      this.checkBoxListener = listener;
    }
  }

  /**
   * コンストラクタ(アクテビティ用)
   * 
   * @param filterType
   * @param activity
   */
  public FilterLogCard(FilterType filterType, PlusActivity activity) {
    this.filterType = filterType;
    this.activity = activity;
    setPanel(filterType);
  }

  /**
   * コンストラクタ(＋１er用)
   * 
   * @param filterType
   * @param plusOner
   */
  public FilterLogCard(FilterType filterType, PlusPeople plusOner) {
    this.filterType = filterType;
    this.plusOner = plusOner;
    setPanel(filterType);
  }

  /**
   * コンストラクタ（キーワード用）
   * 
   * @param filterType
   * @param keyword
   */
  public FilterLogCard(FilterType filterType, String keyword) {
    this.filterType = filterType;
    this.keyword = keyword;
    setPanel(filterType);
  }

  /**
   * パネルのセットアップ
   * 
   * @param filterType
   */
  public void setPanel(FilterType filterType) {
    this.booleans = getBooleansField();
    this.add(booleans);
    this.enableCheck = getEnableCheckBox();
    this.add(enableCheck);
    this.add(new Label(setLabelString(filterType)));
    this.setStyleName(MyStyle.FILTER_CARD.getStyle());
  }

  /**
   * 論理結合子選択フィールドを提供する
   * 
   * @return
   */
  private ListBoxField getBooleansField() {
    ListBoxField booleans = new ListBoxField("", null, BOOLEANS.values());
    booleans.getBasicField().addStyleName(MyStyle.FILTER_BOOLEAN.getStyle());
    booleans.getBasicField().addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        checkBoxListener.onValueChange(getEnableCheck());
      }
    });
    return booleans;

  }

  /**
   * ログラベルの文言作成
   * 
   * @param filterType
   */
  private String setLabelString(FilterType filterType) {
    String labelString = "";
    switch (filterType) {
    case ACTIVITIES_PLUSONER:
      labelString = "+1er=" + plusOner.getDisplayName();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      labelString = "カテゴリ:" + keyword;
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      labelString = keyword + "年";
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      labelString = keyword + "月";
      break;
    case ACTIVITIES_KEYWORD:
      labelString = "アクティビティ:" + keyword;
      break;
    case PLUSONER_ACTIVITY:
      labelString = "アクティビティ=" + activity.getTitle().substring(0, 6);
      break;
    case PLUSONER_KEYWORD:
      labelString = "+1er:" + keyword;
      break;
    default:
      break;
    }
    return labelString;
  }

  /**
   * フィルターイネーブラーチェックボックスを作成する
   * 
   * @return
   */
  private CheckBox getEnableCheckBox() {
    CheckBox checkBox = new CheckBox();
    checkBox.setValue(true);
    checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        setCardStyle();
        checkBoxListener.onValueChange(getEnableCheck());
      }
    });
    return checkBox;
  }

  /**
   * 結合論理演算子 getter
   * 
   * @return
   */
  public BOOLEANS getBooleansValue() {
    return BOOLEANS.values()[(booleans.getSelectedIndex())];
  }

  /**
   * 論理和子セッターの可視性を設定する
   * 
   * @param visible
   */
  public void setBooleansVisible(boolean visible) {
    this.booleans.getBasicField().setVisible(visible);
  }

  /**
   * activity getter
   * 
   * @return
   */
  public PlusActivity getActivity() {
    return activity;
  }

  /**
   * plusOner getter
   * 
   * @return
   */
  public PlusPeople getPlusOner() {
    return plusOner;
  }

  /**
   * keyword getter
   * 
   * @return
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * filter type getter
   * 
   * @return
   */
  public FilterType getFilterType() {
    return filterType;
  }

  /**
   * enable check getter
   * 
   * @return
   */
  public boolean getEnableCheck() {
    return enableCheck.getValue();
  }

  /**
   * 有効無効に応じてカードのスタイルを変更する
   */
  private void setCardStyle() {
    if (enableCheck.getValue()) {
      FilterLogCard.this.removeStyleName(MyStyle.FILTER_DISABLE.getStyle());
      FilterLogCard.this.addStyleName(MyStyle.FILTER_CARD.getStyle());
      setBooleansVisible(true);
    } else {
      FilterLogCard.this.removeStyleName(MyStyle.FILTER_CARD.getStyle());
      FilterLogCard.this.addStyleName(MyStyle.FILTER_DISABLE.getStyle());
      setBooleansVisible(false);
    }
  }
}
