package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.panel.parts.FilterInputPanelFields;
import jp.leopanda.gPlusAnalytics.client.panel.parts.NumOfPlusOneComboField;
import jp.leopanda.gPlusAnalytics.client.panel.parts.PostCategoryListBox;
import jp.leopanda.gPlusAnalytics.client.panel.parts.PublishedYearListBox;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
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
  // フィールド定義
  FilterInputPanelFields fields;
  CheckBox incrementalFilterCheck = new CheckBox();

  /**
   * コンストラクター
   * 
   * @param sourceItems
   */
  public FilterInputPanel(FilterableSourceItems sourceItems) {
    fields = new FilterInputPanelFields(sourceItems);
    setUpFields();
  }

  /**
   * 入力フィールドのセットアップ
   */
  private void setUpFields() {
    TextBoxField plusOnerFilter = fields.getPlusOnerFilter();
    TextBoxField activityFilter = fields.getActivityFilter();
    PostCategoryListBox postCategory = fields.getPostCategory();
    PublishedYearListBox publishedYear = fields.getPublishedYear();
    ListBoxField publishedMonth = fields.getPublishedMonth();
    NumOfPlusOneComboField plusOnerPlusOnes = fields.getPlusOnerPlusOnes();
    NumOfPlusOneComboField activityPlusOnes = fields.getActivityPlusOnes();

    fieldMap.add(plusOnerFilter);
    fieldMap.add(plusOnerPlusOnes.getField());
    fieldMap.add(activityFilter);
    fieldMap.add(activityPlusOnes.getField());
    fieldMap.add(postCategory.getField());
    fieldMap.add(publishedYear.getField());
    fieldMap.add(publishedMonth);

    HorizontalPanel filterLine = new HorizontalPanel();
    filterLine.add(plusOnerFilter);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(plusOnerPlusOnes.getPanel());
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(activityFilter);
    filterLine.add(activityPlusOnes.getPanel());
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(postCategory.getField());
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(publishedYear.getField());
    filterLine.add(publishedMonth);

    this.add(filterLine);

    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    activityFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
  }

  /**
   * リスナーを設定する
   * 
   * @param listener
   */
  public void addFilterRequestListener(FilterRequestListener listener) {
    fields.setRequestListener(listener);
  }

  /**
   * フィールドを初期状態に戻す
   */
  public void resetFields() {
    fieldMap.resetFields();
  }
 }
