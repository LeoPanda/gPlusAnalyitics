package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;
import jp.leopanda.gPlusAnalytics.client.panel.ActivityMiniTable;
import jp.leopanda.gPlusAnalytics.client.panel.parts.ItemListPopPanel;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.calendar.CalendarOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;

/**
 * アクテビティへの＋１数をカレンダー上に表示する
 * 
 * @author LeoPanda
 *
 */
public class ActivityCalendarChart extends SimpleChart<PlusActivity, CalendarOptions> {
  private ItemListPopPanel<PlusActivity, ActivityMiniTable> activitySelectorPanel;

  /**
   * コンストラクタ
   */
  public ActivityCalendarChart() {
    super(ChartType.CALENDAR, ChartPackage.CALENDAR);
    setChartFunction(chart -> getChart(chart));
    setChartOptionsFunction(chartOptions -> getChartOptions(chartOptions));
    setDataTableFunction(dataTable -> getDataTable(dataTable));
  }

  /**
   * チャートの生成
   * 
   * @param chart
   * @return
   */
  private ChartWrapper<CalendarOptions> getChart(ChartWrapper<CalendarOptions> chart) {
    chart.addSelectHandler(getSelectHandler(chart));
    return chart;
  }

  /**
   * カラム選択時ハンドラの生成
   * 
   * @param chart
   * @return
   */
  private SelectHandler getSelectHandler(ChartWrapper<CalendarOptions> chart) {
    return new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        int selectedRowIndex = getSelectedRowIndex(chart);
        if (selectedRowIndex > 0) {
          popActivitySelectorPanel(chart.getDataTable().getValueDate(selectedRowIndex, 0));
        } else {
          Window.alert("この日は投稿がありません。");
        }
      }
    };
  }

  /**
   * 選択行インデックスの取得
   * 
   * @param chart
   * @return
   */
  private int getSelectedRowIndex(ChartWrapper<CalendarOptions> chart) {
    return chart.getSelection().length() > 0
        ? Optional.ofNullable(chart.getSelection().get(0).getRow()).orElse(0) : 0;
  }

  /**
   * 該当日に投稿されたアクテビティの選択パネルを表示する
   * 
   * @param selectedDate
   */
  private void popActivitySelectorPanel(Date selectedDate) {
    closeSelectorPanelIfAlreadyOpen();
    activitySelectorPanel = new ItemListPopPanel<PlusActivity, ActivityMiniTable>(
        getwindowTitle(selectedDate), 10, new ActivityMiniTable(getSourceData()));
    activitySelectorPanel.showWhen(items -> pickupItemsBySelectedDate(selectedDate, items));
  }

  /**
   * 選択パネルがすでに開いていれば閉じる
   */
  private void closeSelectorPanelIfAlreadyOpen() {
    Optional.ofNullable(activitySelectorPanel).ifPresent(panel -> activitySelectorPanel.closePop());
  }

  /**
   * 選択された日付と投稿日付が一致するアクテビティを検出する
   * 
   * @param selectedDate
   * @param item
   * @return
   */
  private boolean pickupItemsBySelectedDate(Date selectedDate, PlusActivity item) {
    return Formatter.getYYMDString(item.getPublished())
        .equals(Formatter.getYYMDString(selectedDate));
  }

  /**
   * 選択パネルのタイトルを生成する
   * 
   * @param selectedDate
   * @return
   */
  private String getwindowTitle(Date selectedDate) {
    return Formatter.getYYMDString(selectedDate) + "のアクテビティ";
  }

  /**
   * グラフオプションの生成
   * 
   * @param chartOptions
   * @return
   */
  private CalendarOptions getChartOptions(CalendarOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    chartOptions.setNoDataPattern("#DDD", "rgba(76, 69, 10, 0.86)");
    return chartOptions;
  }

  /**
   * 表示データのセット
   * 
   * @param dataTable
   * @return
   */
  private DataTable getDataTable(DataTable dataTable) {
    dataTable.addColumn(ColumnType.DATE, "投稿日");
    dataTable.addColumn(ColumnType.NUMBER, "+1数");
    getSummaryNumOfPlusOneByPublishedDay(getSourceData()).forEach((k, v) -> dataTable.addRow(k, v));
    return dataTable;
  }

  /**
   * アクテビティリストから投稿日毎の+1数を集計する
   * 
   * @param activities
   * @return
   */
  private Map<Date, Integer> getSummaryNumOfPlusOneByPublishedDay(List<PlusActivity> activities) {
    return activities.stream().collect(
        Collectors.groupingBy(item -> tranceteTime(item.getPublished()),
            Collectors.reducing(0, PlusActivity::getNumOfPlusOners, Integer::sum)));
  }

  /**
   * 日付フィールドから時間を切り捨てる
   * 
   * @param date
   * @return
   */
  private Date tranceteTime(Date date) {
    DateTimeFormat ymdFormat = DateTimeFormat.getFormat(DateFormat.YYMD.getValue());
    return ymdFormat.parse(ymdFormat.format(date));
  }

}
