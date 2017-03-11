package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.Date;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.panel.ActivitySelectorPop;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

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
  private ActivitySelectorPop activitySelectorPop = null; // アクテビティ選択用ポップアップ画面

  /**
   * コンストラクタ
   */
  public ActivityCalendarChart() {
    super(ChartType.CALENDAR, ChartPackage.CALENDAR);
  }

  /*
   * グラフの作成
   */
  protected ChartWrapper<CalendarOptions> getChart(ChartType chartType) {
    chart = super.getChart(chartType);
    chart.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        if (chart.getSelection().length() > 0) {
          if (chart.getSelection().get(0).getRow() != null) {
            Date selectedDate =
                chart.getDataTable().getValueDate(chart.getSelection().get(0).getRow(), 0);
            showSelector(selectedDate);
          } else {
            Window.alert("この日は投稿がありません。");
          }
        }
      }
    });
    return chart;
  }

  /*
   * グラフのオプションを作成する
   */
  protected CalendarOptions getChartOptions() {
    chartOptions = super.getChartOptions();
    chartOptions.setTitle(getChartTitle());
    chartOptions.setNoDataPattern("#DDD", "rgba(76, 69, 10, 0.86)");
    return chartOptions;
  }

  /*
   * グラフに表示するデータをセットする
   */
  @Override
  protected DataTable getDataTable() {
    dataTable = super.getDataTable();
    dataTable.addColumn(ColumnType.DATE, "投稿日");
    dataTable.addColumn(ColumnType.NUMBER, "+1数");
    PlusOnerCounter counter = new PlusOnerCounter();
    for (PlusActivity activity : sourceData) {
      if (counter.isSamePublished(activity)) {
      } else {
        dataTable = counter.addNumOfPlusOnersByPublished(dataTable);
        counter.published = activity.getPublished();
      }
      counter.countNumOfPlusOners(activity);
    }
    dataTable = counter.addNumOfPlusOnersByPublished(dataTable);
    return dataTable;
  }

  /*
   * アクテビティ選択画面を表示する
   */
  private void showSelector(Date selectedDate) {
    if (activitySelectorPop != null) {
      activitySelectorPop.closePop();
      activitySelectorPop = null;
    }
    activitySelectorPop = new ActivitySelectorPop(selectedDate,sourceData);
    activitySelectorPop.show();
  }

  /**
   * 同一年月日に投稿されたアクテビティの+1数を集計するためのカウンターオブジェクト
   */
  class PlusOnerCounter {
    public Date published = null;
    private int numOfPlusOners = 0;

    /**
     * カウンターに+1er数を加算する
     * @param activity
     */
    public void countNumOfPlusOners(PlusActivity activity) {
      numOfPlusOners += activity.getNumOfPlusOners();
    }

    /**
     * 投稿日付別+1er数をデータテーブルへ書き出す
     * @param dataTable　
     * @return
     */
    public DataTable addNumOfPlusOnersByPublished(DataTable dataTable) {
      dataTable.addRow(published, numOfPlusOners);
      numOfPlusOners = 0;
      return dataTable;
    }

    /**
     * カウンターの累積判定
     * 
     * @param activity 対象のアクティビティレコード
     * @return カウントアップが必要な場合にtrue
     */
    public boolean isSamePublished(PlusActivity activity) {
      if (published == null) {
        published = activity.getPublished();
        return true;
      } else {
        return Formatter.getYYMDString(activity.published).equals(
            Formatter.getYYMDString(published));
      }
    }
  }

}
