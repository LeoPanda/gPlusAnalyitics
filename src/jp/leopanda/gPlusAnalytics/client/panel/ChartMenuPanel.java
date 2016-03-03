package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityCalendarChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityColumnChart;
import jp.leopanda.gPlusAnalytics.client.chart.NumOfPlusOnePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PlusOnersPieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PostCirclePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.abstracts.ChartOnMenu;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
  private Label filterLogPanel;
  private HorizontalPanel chartPanel;

  private List<PlusActivity> activitySourceItems;//チャート表示するアクティビティアイテムのソースデータ
  private List<PlusPeople> plusOnersSourceItems;//チャート表示する+1erアイテムのソースデータ

  private Map<ChartInfo, ChartOnMenu<?>> charts = new HashMap<ChartInfo, ChartOnMenu<?>>(); // チャートのインスタンス
  private List<ChartInfo> onMenuChart = new ArrayList<ChartInfo>(); // 現在表示中のチャート

  private final int chartPanelMaxColumn = 2; // チャートパネルの最大カラム数
  private int chartPanelColumns = 0; // チャートパネルの現在カラム数

  /**
   * コンストラクタ
   */
  public ChartMenuPanel(List<PlusActivity> activitySoruceItems,List<PlusPeople> plusOnerSourceItems) {
    this.activitySourceItems = activitySoruceItems;
    this.plusOnersSourceItems = plusOnerSourceItems;
    this.setWidth("1600px");
    this.add(getLinkPanel());
    this.add(getChartPanel());
  }

  /*
   * リンク表示用パネルの作成
   */
  private VerticalPanel getLinkPanel() {
    if (linkPanel == null) {
      linkPanel = new VerticalPanel();
      for (ChartInfo chart : ChartInfo.values()) {
        linkPanel.add(new HTML("<br/>"));
        Anchor link = new Anchor(chart.title);
        addLinkClickHandler(link, chart);
        linkPanel.add(link);
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
      filterLogPanel = new Label();
      filterLogPanel.setStyleName(CssStyle.LABEL_FILTER.getName());
      chartPanel = new HorizontalPanel();
    }
    centerPanel.add(filterLogPanel);
    centerPanel.add(chartPanel);
    return centerPanel;
  }

  /*
   * アンカークリックハンドラの追加
   */
  private void addLinkClickHandler(Anchor link, final ChartInfo chartInfo) {
    link.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (Global.getActivityItems() == null || Global.getPlusOners() == null) {
          Window.alert("データの読み込みが終わるまでしばらくお待ち下さい。");
        } else {
          addCharrtToPanel(chartInfo);
        }
      }

    });

  }

  /*
   * チャートをパネルに追加する
   */
  private void addCharrtToPanel(ChartInfo chartInfo) {
    if (onMenuChart.contains(chartInfo)) {
      return;
    }
    if (chartPanelColumns + chartInfo.occupiedColum > chartPanelMaxColumn) {
      onMenuChart.clear();
      chartPanel.clear();
      chartPanelColumns = 0;
    }
    onMenuChart.add(chartInfo);
    chartPanel.add(getChartInstance(chartInfo));
    chartPanelColumns += chartInfo.occupiedColum;
  }

  /*
   * チャートのインスタンスを取得する
   */
  private ChartOnMenu<?> getChartInstance(ChartInfo chartInfo) {
    if (charts.get(chartInfo) == null) {
      charts.put(chartInfo, getNewWidget(chartInfo));
    }
    return charts.get(chartInfo);
  }

  /*
   * チャートを生成する
   */
  private ChartOnMenu<?> getNewWidget(ChartInfo chartInfo) {
    ChartOnMenu<?> newChart = null;
    switch (chartInfo) {
      case ACTIVIY_COLUMN:
        newChart = setChart(new ActivityColumnChart(), activitySourceItems, chartInfo);
        break;

      case ACTIVITY_CALENDAR:
        newChart = setChart(new ActivityCalendarChart(), activitySourceItems, chartInfo);
        break;

      case NUM_OF_PLUSONE:
        newChart = setChart(new NumOfPlusOnePieChart(), activitySourceItems, chartInfo);
        break;

      case PLUSONRES_PIE:
        newChart = setChart(new PlusOnersPieChart(), plusOnersSourceItems, chartInfo);
        break;

      case POSTCIRCLE_PIE:
        newChart = setChart(new PostCirclePieChart(), activitySourceItems, chartInfo);
        break;
      // case GENDER_PIE :
      // newChart = new GenderPieChart();
      // break;
      //
      // case LANGUAGE_PIE :
      // newChart = new LanguagePieChart();
      // break;

      default:
        break;
    }
    return newChart;
  }

  /*
   * 生成したチャートに諸元を与える
   */
  private <I extends PlusItem> ChartOnMenu<I> setChart(ChartOnMenu<I> chart, List<I> sourceItems,
      ChartInfo chartInfo) {
    chart.setMenuInfo(chartInfo);
    chart.draw(sourceItems);
    return chart;
  }

  /**
   * メニュー上にあるチャートを再描画する
   * 
   * @param filterLog 表のフィルター履歴
   */
  public void reloadChartDataTables(String filterLog) {
    filterLogPanel.setText(filterLog);
    for (Map.Entry<ChartInfo, ChartOnMenu<?>> entry : charts.entrySet()) {
      entry.getValue().reDraw();
    }
  }

}
