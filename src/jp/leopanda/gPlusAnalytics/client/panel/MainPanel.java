package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
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
    filterLogPanel.addRestButtonListener(getResetButtonListener());
    this.add(menuTabPanel);
  }

  /**
   * フィルター入力パネルからフィルターリクエストがあった場合の処理
   * 
   * @return
   */
  private FilterRequestListener getFilterInputPanelRequestListener() {
    return (filterType, keyword) -> {
      addFilterLog(filterType, keyword);
      reloadPanel();
    };
  }

  /**
   * テーブルパネルでフィルターボタンが押された場合の処理
   * 
   * @return
   */
  private ItemClickListener<PlusItem> getTablePanelClickListener() {
    return (plusItem,filterType) -> {
      addFilterLog(filterType, plusItem);
      reloadPanel();
    };
  }

  /**
   * フィルターログにカードを追加する
   * 
   * @param filterType
   * @param keyword
   */
  private void addFilterLog(FilterType filterType, Object keyword) {
    filterLogPanel.addFilterLogCard(filterType, keyword,v -> reloadPanel());
  }

  /**
   * ログパネルでリセットボタンが押された時の処理
   * 
   * @return
   */
  private FilterRequestListener getResetButtonListener() {
    return (filterType, keyword) -> {
      if (filterType == FilterType.RESET_ITEMS) {
        resetFilter();
        reloadPanel();
      }
    };
  }

  /**
   * フィルターをリセットする
   */
  private void resetFilter() {
    sourceItems.resetItems();
    filterLogPanel.clear();
  }

  /**
   * メインパネルのリロード
   */
  private void reloadPanel() {
    sourceItems.doFilterEachCards(filterLogPanel);
    filterInputPanel.resetFields();
    menuTabPanel.reloadItems();
  }

}
