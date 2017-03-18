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
  MenuPanel menuPanel;

  /**
   * コンストラクタ
   * 
   * @param plusActivities
   * @param plusOners
   */
  public MainPanel(List<PlusActivity> plusActivities, List<PlusPeople> plusOners) {

    sourceItems = new FilterableSourceItems(plusActivities, plusOners);

    filterInputPanel = new FilterInputPanel(sourceItems);
    filterLogPanel = new FilterLogPanel(sourceItems);
    menuPanel = new MenuPanel(sourceItems);

    addListeners();
    setupPanel();

  }

  /**
   * パネルの構成
   */
  private void setupPanel() {
    this.add(filterInputPanel);
    this.add(filterLogPanel);
    this.add(menuPanel);
  }

  /**
   * 各パネルのリスナー設定
   */
  private void addListeners() {
    // メニューパネルでフィルターボタンが押された場合の処理
    menuPanel.addItemClickListener(new ItemClickListener<PlusItem>() {
      @Override
      public void onClick(PlusItem item) {
        if (item instanceof PlusActivity) {
          sourceItems.filterPlusOnersByActivity((PlusActivity) item);
        } else if (item instanceof PlusPeople) {
          sourceItems.filterActiviesByPlusOner((PlusPeople) item);
        }
        menuPanel.reloadItems();
        filterLogPanel.displayLog();
      }
    });
    
    // フィルター入力画面からのリクエスト処理
    filterInputPanel.addFilterRequestListener(new FilterRequestListener() {
      @Override
      public void request(FilterType filterType) {
        if(!filterInputPanel.getIncrimentalFilterCheck()){
          sourceItems.resetItems();
        }
        switch (filterType) {
        case ACTIVITIES_KEYWORD:
          sourceItems.filterActivitiesByKeyword(filterInputPanel.getActivityKeyword());
          break;
        case ACTIVITIES_PUBLISHED_YEAR:
          sourceItems.filterActivitesByPublishedYear(filterInputPanel.getFilterYear());
          break;
        case ACTIVITIES_PUBLISHED_MONTH:
          sourceItems.filterActivitesByPublishedMonth(filterInputPanel.getFilterMonth());
          break;
        case ACTIVITIES_ACCESSDESCRIPTION:
          sourceItems.filterActivitiesByAccessDescription(filterInputPanel.getPostCategoryKeyword());
          break;
        case PLUSONER_KEYWORD:
          sourceItems.filterPlusOnersByKeyword(filterInputPanel.getPlusOnerKeyword());
          break;
        case RESET_ITEMS:
          sourceItems.resetItems();
          filterInputPanel.resetFields();
        default:
          break;
        }
        menuPanel.reloadItems();
        filterLogPanel.displayLog();

      }
    });
    
  }
}
