package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.client.enums.Month;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.ListBoxField;
import jp.leopanda.panelFrame.filedParts.TextBoxField;
import jp.leopanda.panelFrame.panelParts.PanelBase;

/**
 * アイテムのフィルターを指示する入力パネル
 * 
 * @author LeoPanda
 *
 */
public class FilterInputPanel extends PanelBase {

  // フィールド
  TextBoxField plusOnerFilter;
  TextBoxField activityFilter;
  PublishedYearListBox publishedYear;
  PostCategoryListBox postCategory;
  ListBoxField publishedMonth;
  // フィルターリクエストボタン
  RequestButton plusOnerFilterButton = new RequestButton("▶",
      FilterType.PLUSONER_KEYWORD);
  RequestButton activityFilterButton = new RequestButton("▶",
      FilterType.ACTIVITIES_KEYWORD);
  RequestButton resetButton = new RequestButton("☓", FilterType.RESET_ITEMS);
  CheckBox incrementalFilterCheck = new CheckBox();
  // リスナー
  FilterRequestListener requestListener;

  /**
   * コンストラクター
   * 
   * @param sourceItems
   */
  public FilterInputPanel(FilterableSourceItems sourceItems) {
    setUpFields(sourceItems);
    setPanel();
  }

  /**
   * 入力フィールドのセットアップ
   */
  private void setUpFields(FilterableSourceItems sourceItems) {
    plusOnerFilter = new TextBoxField("+1er:", null);
    activityFilter = new TextBoxField("アクテビティ:", null);
    postCategory = new PostCategoryListBox("投稿先:", sourceItems.getActivities());
    publishedYear = new PublishedYearListBox("投稿日付:", sourceItems.getActivities());
    publishedMonth = new ListBoxField(" ", null, Month.values());

    fieldMap.add(plusOnerFilter);
    fieldMap.add(activityFilter);
    fieldMap.add(postCategory.getField());
    fieldMap.add(publishedYear.getField());
    fieldMap.add(publishedMonth);

    postCategory.addEventListener(new OnValueChange(FilterType.ACTIVITIES_ACCESSDESCRIPTION));
    publishedYear.addEventListener(new OnValueChange(FilterType.ACTIVITIES_PUBLISHED_YEAR));
    publishedMonth.addEventListener(new OnValueChange(FilterType.ACTIVITIES_PUBLISHED_MONTH));
  }

  /**
   * パネルの設定
   */
  private void setPanel() {
    HorizontalPanel filterLine = new HorizontalPanel();
    filterLine.add(plusOnerFilter);
    filterLine.add(plusOnerFilterButton);
    filterLine.add(activityFilter);
    filterLine.add(activityFilterButton);
    filterLine.add(postCategory.getField());
    filterLine.add(publishedYear.getField());
    filterLine.add(publishedMonth);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(resetButton);
    filterLine.add(getCheckBox());

    this.add(filterLine);
    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    activityFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    resetButton.addStyleName(MyStyle.RESET_BUTTON.getStyle());
  }

  /**
   * 累積フィルターチェックボックスを設定する
   * 
   * @return
   */
  private HorizontalPanel getCheckBox() {
    HorizontalPanel checkBoxPanel = new HorizontalPanel();
    checkBoxPanel.add(incrementalFilterCheck);
    Label title = new Label("フィルター累積");
    title.addStyleName(MyStyle.CHECKBOX_LABEL.getStyle());
    checkBoxPanel.add(title);
    return checkBoxPanel;
  }

  /**
   * リスナーを設定する
   * 
   * @param listener
   */
  public void addFilterRequestListener(FilterRequestListener listener) {
    this.requestListener = listener;
  }

  /**
   * フィールドを初期状態に戻す
   */
  public void resetFields() {
    fieldMap.resetFields();
    incrementalFilterCheck.setValue(false);
  }

  /**
   * フィルター条件キーワードを取得する
   * 
   * @param filterType
   * @return
   */
  private String getFilterKeyword(FilterType filterType) {
    String keyword = null;
    switch (filterType) {
    case PLUSONER_KEYWORD:
      keyword = plusOnerFilter.getText();
      break;
    case ACTIVITIES_KEYWORD:
      keyword = activityFilter.getText();
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      keyword = publishedYear.getValue();
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      keyword = Month.values()[publishedMonth.getSelectedIndex()].getNumber();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      keyword = postCategory.getValue();
      break;
    default:
      break;
    }
    return keyword;
  }

  /**
   * 累積フィルターチェックの有無を取得する
   * 
   * @return
   */
  public boolean getIncrimentalFilterCheck() {
    return incrementalFilterCheck.getValue();
  }

  /**
   * フィルターリクエストボタン
   * 
   * @author LeoPanda
   *
   */
  private class RequestButton extends Button {
    public RequestButton(String title, final FilterType filterType) {
      super(title);
      this.addStyleName(MyStyle.FILTER_BUTTON.getStyle());
      this.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          requestListener.request(filterType, getFilterKeyword(filterType));
        }
      });
    }
  }

  /**
   * リストボックス値変更時アクション定義クラス
   * 
   * @author LeoPanda
   *
   */
  private class OnValueChange implements EventAction {
    FilterType filterType;

    public OnValueChange(FilterType filterType) {
      this.filterType = filterType;
    }

    @Override
    public void onValueChange() {
      requestListener.request(filterType, getFilterKeyword(filterType));
    }

  }

}
