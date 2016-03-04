package jp.leopanda.gPlusAnalytics.client.chart.abstracts;


import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlOptions;
import com.googlecode.gwt.charts.client.controls.ControlState;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.ControlWrapper;
import com.googlecode.gwt.charts.client.controls.Dashboard;
import com.googlecode.gwt.charts.client.options.Options;

/**
 * フィルター付きチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class FilterdChart<I extends PlusItem, O extends Options,
    C extends ControlOptions<?>, S extends ControlState>
    extends SimpleChart<I, O> {
  private Dashboard dashboard;
  private ControlWrapper<C, S> rangeFilter;
  protected C filterOptions;
  protected S filterState;
  private ControlType controlType;

  /**
   * コンストラクタ <br/>
   * ChartPackage.CONTROLS使用時にはChartype.CALENDARは使用できません。
   * 
   * @param chartType チャートタイプ
   * @param controlTyle チャートコントロールタイプ
   */
  public FilterdChart(ChartType chartType, ControlType controlTyle) {
    super(chartType, ChartPackage.CONTROLS);
    this.controlType = controlTyle;
  }

  @Override
  protected void addChartToPanel(ChartType chartType) {
    add(getDashboardWidget());
    super.addChartToPanel(chartType);
    add(getRangeFilter(controlType));
  }

  /*
   * チャートを描画する
   */
  @Override
  protected void drawCore() {
    // レンジフィルターの設定
    setRangeFilter();
    if (filterState != null) {
      rangeFilter.setState(filterState);
    }
    if (filterOptions != null) {
      rangeFilter.setOptions(filterOptions);
    }
    // グラフの設定
    chart.setOptions(getChartOptions());
    // グラフ描画
    dashboard.bind(rangeFilter, chart);
    dashboard.draw(getDataTable());

  }

  /*
   * ダッシュボードの作成
   */
  private Dashboard getDashboardWidget() {
    if (dashboard == null) {
      dashboard = new Dashboard();
    }
    return dashboard;
  }

  /*
   * レンジフィルターの作成
   */
  private ControlWrapper<C, S> getRangeFilter(ControlType controlType) {
    if (rangeFilter == null) {
      rangeFilter = new ControlWrapper<C, S>();
      rangeFilter.setControlType(controlType);
    }
    return rangeFilter;
  }

  /**
   * レンジフィルターオプションの作成
   * 
   * @return レンジフィルターオプション
   */
  protected abstract C getfilterOptions();

  /**
   * レンジフィルターステートの作成
   * 
   * @return レンジフィルターオプションステート
   */
  protected abstract S getFilterState();

  /**
   * レンジフィルターの設定 <br/>
   * getFilterOptions あるいは getFilterlStateを呼び出し<br/>
   * レンジフィルターに必要なオプションとステートを設定する。
   */
  protected abstract void setRangeFilter();

}
