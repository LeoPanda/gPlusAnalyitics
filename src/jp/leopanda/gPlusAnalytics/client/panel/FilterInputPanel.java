package jp.leopanda.gPlusAnalytics.client.panel;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.Month;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
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
  RequestButton postCategoryFilterButton = new RequestButton("▶",
      FilterType.ACTIVITIES_ACCESSDESCRIPTION);
  RequestButton publishedFilterButton = new RequestButton("▶",
      FilterType.ACTIVITIES_PUBLISHED);
  RequestButton resetButton = new RequestButton("☓", FilterType.RESET_ITEMS);
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
    filterLine.add(postCategoryFilterButton);
    filterLine.add(publishedYear.getField());
    filterLine.add(publishedMonth);
    filterLine.add(publishedFilterButton);
    filterLine.add(new HTML("<br/>"));
    filterLine.add(resetButton);

    this.add(filterLine);
    plusOnerFilter.addStyleName(MyStyle.FILTER_LABEL.getStyle());
    activityFilter.addStyleName(MyStyle.FILTER_LABEL.getStyle());
    plusOnerFilter.addStyleName(MyStyle.FILTER_LABEL.getStyle());
    resetButton.addStyleName(MyStyle.RESET_BUTTON.getStyle());
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
  }

  /**
   * +1erフィルターキーワードを取得する
   * 
   * @return
   */
  public String getPlusOnerKeyword() {
    return plusOnerFilter.getText();
  }

  /**
   * アクテビティフィルターキーワードを取得する
   * 
   * @return
   */
  public String getActivityKeyword() {
    return activityFilter.getText();
  }

  /**
   * 投稿先キーワードを取得する
   * 
   * @return
   */
  public String getPostCategoryKeyword() {
    return postCategory.getValue();
  }

  /**
   * アクテビティフィルター投稿年キーワードを取得する
   * 
   * @return
   */
  public String getFilterYear() {
    return publishedYear.getValue();
  }

  /**
   * アクテビティフィルター投稿月キーワードを取得する
   * 
   * @return
   */
  public String getFilterMonth() {
    return Month.values()[publishedMonth.getSelectedIndex()].getNumber();
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
          requestListener.request(filterType);
        }
      });
    }
  }
}
