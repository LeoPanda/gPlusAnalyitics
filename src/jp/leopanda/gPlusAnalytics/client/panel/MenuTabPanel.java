package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * メインメニュー
 * 
 * @author LeoPanda
 *
 */
public class MenuTabPanel extends TabPanel {
  private TableLaunchPanel tablePanel; // リスト表パネル
  private PhotoPanel photoPanel; // 写真パネル
  private ChartMenuPanel chartPanel;// 図表パネル
  private MaintenancePanel mentePanel; // データメンテナンスパネル
  private ItemClickListener<PlusItem> itemClickListener;

  /**
   * コンストラクタ
   */
  public MenuTabPanel(FilterableSourceItems sourceItems) {

    tablePanel = new TableLaunchPanel(sourceItems);
    photoPanel = new PhotoPanel(sourceItems);
    chartPanel = new ChartMenuPanel(sourceItems);
    mentePanel = new MaintenancePanel();

    tablePanel.addItemEventListener(new ItemClickListener<PlusItem>() {
      @Override
      public void onClick(PlusItem item) {
        itemClickListener.onClick(item);
      }
    });
    this.addSelectionHandler(event -> {
      if (event.getSelectedItem() == 1) {
        photoPanel.reload();
      }
    });

    this.add(tablePanel, "tables");
    this.add(photoPanel, "photos");
    this.add(chartPanel, "chart");
    this.add(mentePanel, "maintenance");
    this.selectTab(0);
  }

  /**
   * テーブルメニュー上でのフィルターイベントを拾うリスナーを設定する
   * 
   * @param filterEventListener
   */
  public void addItemClickListener(ItemClickListener<PlusItem> itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  /**
   * パネルのリロード
   */
  public void reloadItems() {
    tablePanel.reloadItems();
    chartPanel.reDrawCharts();
    photoPanel.reload();
  }
}
