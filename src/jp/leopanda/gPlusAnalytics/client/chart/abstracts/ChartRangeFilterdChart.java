package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterState;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterStateRange;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterUi;
import com.googlecode.gwt.charts.client.options.Options;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * チャートレンジフィルター付きチャート
 * 
 * @author LeoPanda
 *
 * @param <O>
 *          チャートオプション
 */
public abstract class ChartRangeFilterdChart<I extends PlusItem, O extends Options> extends
    FilterdChart<I, O, ChartRangeFilterOptions, ChartRangeFilterState> {
  private int miniRangeWidth; // レンジフィルターの最小レンジ幅
  private int maxRangeValue; // レンジフィルターの最大値
  private int defaultRangeWidth; // レンジフィルターのデフォルト幅
  protected ChartRangeFilterUi filterUi;// レンジフィルターチャートのUIオプション
  protected ChartRangeFilterStateRange stateRange; // レンジフィルターのレンジ幅定義体

  /**
   * コンストラクタ
   * 
   * @param chartType
   *          チャートタイプ
   */
  public ChartRangeFilterdChart(ChartType chartType) {
    super(chartType, ControlType.CHART_RANGE_FILTER);

  }

  /*
   * レンジフィルターの初期設定
   */
  @Override
  protected void setRangeFilter() {
    filterOptions = getfilterOptions();
    filterUi = getFilterUi();
    filterOptions.setUi(filterUi);
    filterState = getFilterState();
    filterState.setRange(setStateRange(maxRangeValue - defaultRangeWidth, maxRangeValue));
  }

  /*
   * レンジフィルターチャートのUIオプションをカラムチャート用にセットする
   */
  private ChartRangeFilterUi getFilterUi() {
    if (filterUi == null) {
      filterUi = ChartRangeFilterUi.create();
      filterUi.setMinRangeSize(miniRangeWidth);
    }
    return filterUi;
  }

  /*
   * グラフのレンジ幅を設定する
   */
  private ChartRangeFilterStateRange setStateRange(int startRange, int endRange) {
    stateRange = ChartRangeFilterStateRange.create();
    stateRange.setStart(startRange);
    stateRange.setEnd(endRange);
    return stateRange;
  }

  /*
   * レンジフィルターオプションの作成
   */
  @Override
  protected ChartRangeFilterOptions getfilterOptions() {
    if (filterOptions == null) {
      filterOptions = ChartRangeFilterOptions.create();
    }
    return filterOptions;
  }

  /**
   * レンジフィルターステートの作成
   * 
   */
  @Override
  protected ChartRangeFilterState getFilterState() {
    if (filterState == null) {
      filterState = ChartRangeFilterState.create();
    }
    return filterState;
  }

  /*
   * レンジ最小幅のセッター
   */
  protected void setMiniRangeWidth(int miniRangeWidth) {
    this.miniRangeWidth = miniRangeWidth;
  }

  /*
   * 最大レンジ値のセッター
   */
  protected void setMaxRangeValue(int maxRangeValue) {
    this.maxRangeValue = maxRangeValue;
  }

  /*
   * デフォルトレンジ幅のセッター
   */
  protected void setDefaultRangeWidth(int defaultRangeWidth) {
    this.defaultRangeWidth = defaultRangeWidth;
  }

}
