package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.chart.ActivityCalendarChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityColumnChart;
import jp.leopanda.gPlusAnalytics.client.chart.NumOfPlusOnePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PlusOnersPieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PostCirclePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.abstracts.ChartBase;
import jp.leopanda.gPlusAnalytics.client.enums.ChartType;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * チャートメニュー
 * 
 * @author LeoPanda
 *
 */
public class ChartMenuPanel extends HorizontalPanel {

  private VerticalPanel linkPanel;
  private VerticalPanel centerPanel;
  private HorizontalPanel chartPanel;

  private FilterableSourceItems sourceItems;

  private Map<ChartType, ChartBase<?>> chartInstances = new HashMap<ChartType, ChartBase<?>>(); // チャートのインスタンス
  private List<ChartType> launchingChartTypes = new ArrayList<ChartType>(); // 現在表示中のチャート種類

  private final int chartPanelMaxColumn = 2; // チャートパネルの最大カラム数
  private int chartPanelColumns = 0; // チャートパネルの現在カラム数

  /**
   * コンストラクタ
   */
  public ChartMenuPanel(FilterableSourceItems sourceItems) {
    this.sourceItems = sourceItems;
    this.setWidth("1600px");
    this.add(getMenuLinkPanel());
    this.add(getChartPanel());
  }

  /*
   * メニューリンク表示用パネルの作成
   */
  private VerticalPanel getMenuLinkPanel() {
    if (linkPanel == null) {
      linkPanel = new VerticalPanel();
      for (ChartType chart : ChartType.values()) {
        linkPanel.add(new HTML("<br/>"));
        linkPanel.add(new MenuAnchor(chart.title, chart));
      }
    }
    return linkPanel;
  }

  /*
   * チャート表示用パネルの作成
   */
  private VerticalPanel getChartPanel() {
    if (chartPanel == null) {
      centerPanel = new VerticalPanel();
      chartPanel = new HorizontalPanel();
    }
    centerPanel.add(chartPanel);
    return centerPanel;
  }

  /*
   * チャートをパネルに表示する
   */
  private void launchChart(ChartType chartType) {
    if (launchingChartTypes.contains(chartType)) {
      return;
    }
    if (chartPanelColumns + chartType.occupiedColum > chartPanelMaxColumn) {
      launchingChartTypes.clear();
      chartPanel.clear();
      chartPanelColumns = 0;
    }
    launchingChartTypes.add(chartType);
    chartPanel.add(getChart(chartType));
    chartPanelColumns += chartType.occupiedColum;
  }

  /*
   * チャートを取得する
   */
  private ChartBase<?> getChart(ChartType chartType) {
    if (chartInstances.get(chartType) == null) {
        chartInstances.put(chartType, getChartInstance(chartType));
    }
    return chartInstances.get(chartType);
  }


  /*
   * チャートを生成する
   */
  private ChartBase<?> getChartInstance(ChartType chartType) {
    ChartBase<?> chartInstance = null;
    switch (chartType) {
    case ACTIVIY_COLUMN:
      chartInstance = newChart(new ActivityColumnChart(), chartType);
      break;

    case ACTIVITY_CALENDAR:
      chartInstance = newChart(new ActivityCalendarChart(), chartType);
      break;

    case NUM_OF_PLUSONE:
      chartInstance = newChart(new NumOfPlusOnePieChart(), chartType);
      break;

    case PLUSONRES_PIE:
      chartInstance = newChart(new PlusOnersPieChart(), chartType);
      break;

    case POSTCIRCLE_PIE:
      chartInstance = newChart(new PostCirclePieChart(), chartType);
      break;

    default:
      break;
    }
    return chartInstance;
  }

  /*
   * チャートを生成し描画する
   */
  private <I extends PlusItem> ChartBase<I> newChart(ChartBase<I> chartInstance, ChartType chartType) {
    chartInstance.setChartType(chartType);
    chartInstance.setSourceData(sourceItems.getItemList(chartType.getItemType()));
    chartInstance.draw();
    return chartInstance;
  }

  /**
   * インスタンス化されたチャートを現在データで再描画する
   */
  public void reloadChartDataTables() {
    for (Map.Entry<ChartType, ChartBase<?>> entry : chartInstances.entrySet()) {
      ChartBase<?> chartInstance = entry.getValue();
      chartInstance.setSourceData(sourceItems.getItemList(entry.getKey().getItemType()));
      chartInstance.reDraw();
    }
  }

  /**
   * チャート選択用リンクアンカー
   * 
   * @author LeoPanda
   *
   */
  private class MenuAnchor extends Anchor {
    MenuAnchor(String title, final ChartType chartType) {
      super(title);
      this.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          launchChart(chartType);
        }
      });
    }
  }

}
