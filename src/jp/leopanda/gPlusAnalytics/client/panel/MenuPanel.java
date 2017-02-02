package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.TableEventListener;

import java.util.List;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * メインメニュー
 * 
 * @author LeoPanda
 *
 */
public class MenuPanel extends TabPanel {
  private TableLaunchPanel tablePanel; // リスト表パネル
  private ChartMenuPanel chartPanel;// 図表パネル
  private MaintenancePanel mentePanel; // データメンテナンスパネル

  /**
   * コンストラクタ
   */
  public MenuPanel(List<PlusActivity> activityItems, List<PlusPeople> plusOners) {
    tablePanel = new TableLaunchPanel(activityItems, plusOners);
    chartPanel = new ChartMenuPanel(activityItems, plusOners);
    mentePanel = new MaintenancePanel();
    tablePanel.addEventListener(new TableEventListener() {
      @Override
      public void onFilter(String filterLog) {
        chartPanel.reloadChartDataTables(filterLog);
      }
    });
    this.add(tablePanel, "tables");
    this.add(chartPanel, "chart");
    this.add(mentePanel, "maintenance");
    this.selectTab(0);
  }

}
