package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.CheckBoxListener;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

/**
 * メインパネル
 * 
 * @author LeoPanda
 *
 */
public class MainPanel extends VerticalPanel {
  FilterableSourceItems sourceItems;
  FilterInputPanel filterInputPanel;
  FilterLogPanel filterLogPanel;
  MenuTabPanel menuTabPanel;

  /**
   * コンストラクタ
   * 
   * @param plusActivities
   * @param plusOners
   */
  public MainPanel(List<PlusActivity> plusActivities, List<PlusPeople> plusOners) {

    sourceItems = new FilterableSourceItems(plusActivities, plusOners);

    filterInputPanel = new FilterInputPanel(sourceItems);
    filterLogPanel = new FilterLogPanel();
    menuTabPanel = new MenuTabPanel(sourceItems);

    filterInputPanel.addFilterRequestListener(getFilterInputPanelRequestListener());
    menuTabPanel.addItemClickListener(getTablePanelClickListener());

    setupPanel();

  }

  /**
   * パネルの構成
   */
  private void setupPanel() {
    this.add(filterInputPanel);
    this.add(filterLogPanel);
    this.add(menuTabPanel);
  }

  /**
   * フィルター入力パネルからフィルターリクエストがあった場合の処理
   * 
   * @return
   */
  private FilterRequestListener getFilterInputPanelRequestListener() {
    return new FilterRequestListener() {
      @Override
      public void request(FilterType filterType, String keyword) {

        if (!filterInputPanel.isIncrimentalChecked()) {
          resetFilter();
        }
        addFilterLog(filterType, keyword);
        reloadPanel();

      }
    };
  }

  /**
   * テーブルパネルでフィルターボタンが押された場合の処理
   * 
   * @return
   */
  private ItemClickListener<PlusItem> getTablePanelClickListener() {
    return new ItemClickListener<PlusItem>() {
      @Override
      public void onClick(PlusItem item) {

        if (!filterInputPanel.isIncrimentalChecked()) {
          resetFilter();
        }
        addFilterLog(null, item);
        reloadPanel();
      }
    };
  }

  /**
   * フィルターログにカードを追加する
   * 
   * @param keyword
   */
  private void addFilterLog(FilterType filterType, Object keyword) {
    if (keyword instanceof PlusActivity) {
      filterLogPanel.add(new FilterLogCard(FilterType.PLUSONER_ACTIVITY, (PlusActivity) keyword));
    } else if (keyword instanceof PlusPeople) {
      filterLogPanel.add(new FilterLogCard(FilterType.ACTIVITIES_PLUSONER, (PlusPeople) keyword));
    } else if (keyword instanceof String) {
      filterLogPanel.add(new FilterLogCard(filterType, (String) keyword));
    } else {
      return;
    }
    filterLogPanel.addCardCheckBoxListerer(getCardCheckBoxListener());
    filterLogPanel.addRestButtonListener(getResetButtonListener());
  }

  /**
   * ログパネルでカードのチェックボックスが変更された場合の処理
   * 
   * @return
   */
  private CheckBoxListener getCardCheckBoxListener() {
    return new CheckBoxListener() {

      @Override
      public void onValueChange(boolean value) {
        reloadPanel();
      }
    };
  }

  /**
   * ログパネルでリセットボタンが押された時の処理
   * 
   * @return
   */
  private FilterRequestListener getResetButtonListener() {
    return new FilterRequestListener() {

      @Override
      public void request(FilterType filterType, String keyword) {
        if (filterType == FilterType.RESET_ITEMS) {
          resetFilter();
          reloadPanel();
        }
      }
    };
  }

  /**
   * フィルターをリセットする
   */
  private void resetFilter() {
    sourceItems.resetItems();
    filterLogPanel.clear();
    filterInputPanel.resetIncrementalCheckBox();
  }

  /**
   * メインパネルのリロード
   */
  private void reloadPanel() {
    sourceItems.doFilter(filterLogPanel);
    filterInputPanel.resetFields();
    menuTabPanel.reloadItems();
  }

}
