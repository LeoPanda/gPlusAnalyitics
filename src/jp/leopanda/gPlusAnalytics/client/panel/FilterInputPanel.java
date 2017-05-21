package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
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
    ListBoxField compOperator = fields.getCompOperator();
    TextBoxField numOfPlusOneFilter = fields.getNumOfPlusOneFilter();
    TextBoxField activityFilter = fields.getActivityFilter();
    PostCategoryListBox postCategory = fields.getPostCategory();
    PublishedYearListBox publishedYear = fields.getPublishedYear();
    ListBoxField publishedMonth = fields.getPublishedMonth();

    fieldMap.add(plusOnerFilter);
    fieldMap.add(compOperator);
    fieldMap.add(numOfPlusOneFilter);
    fieldMap.add(activityFilter);
    fieldMap.add(postCategory.getField());
    fieldMap.add(publishedYear.getField());
    fieldMap.add(publishedMonth);

    HorizontalPanel filterLine = new HorizontalPanel();
    filterLine.add(plusOnerFilter);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(compOperator);
    filterLine.add(numOfPlusOneFilter);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(activityFilter);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(postCategory.getField());
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(publishedYear.getField());
    filterLine.add(publishedMonth);
    filterLine.add(new HTML(FixedString.BLANK_CELL.getValue()));
    filterLine.add(getCheckBoxArea());

    this.add(filterLine);

    numOfPlusOneFilter.addStyleName(MyStyle.FILTER_NUMERIC.getStyle());
    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    activityFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
    plusOnerFilter.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
  }

  /**
   * フィルター累積チェックボックスの入力領域を作成する
   * 
   * @return
   */
  private HorizontalPanel getCheckBoxArea() {
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
    fields.setRequestListener(listener);
  }

  /**
   * フィールドを初期状態に戻す
   */
  public void resetFields() {
    fieldMap.resetFields();
  }

  /**
   * 累積フィルターチェックをリセットする
   */
  public void resetIncrementalCheckBox() {
    incrementalFilterCheck.setValue(false);
  }

  /**
   * 累積フィルターチェックの有無を取得する
   * 
   * @return
   */
  public boolean isIncrimentalChecked() {
    return incrementalFilterCheck.getValue();
  }
}
