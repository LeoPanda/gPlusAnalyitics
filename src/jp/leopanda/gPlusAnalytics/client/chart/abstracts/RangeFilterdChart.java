package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.Optional;
import java.util.function.UnaryOperator;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterState;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterStateRange;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterUi;
import com.googlecode.gwt.charts.client.options.Options;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * 全体表示用サブチャート付きチャート
 * 
 * @author LeoPanda
 *
 * @param <O> チャートオプション
 */
public abstract class RangeFilterdChart<I extends PlusItem, O extends Options> extends
    FilterdChart<I, O, ChartRangeFilterOptions, ChartRangeFilterState> {
  private int miniRangeWidth; // レンジフィルターの最小レンジ幅
  private int maxRangeWidth; // レンジフィルターの最大値
  private int defaultRangeWidth; // レンジフィルターのデフォルト幅
  private UnaryOperator<ChartRangeFilterUi> rangeFilterUiSetter;

  /**
   * コンストラクタ
   * 
   * @param chartType チャートタイプ
   */
  public RangeFilterdChart(ChartType chartType) {
    super(chartType, ControlType.CHART_RANGE_FILTER);
    setRangeFilterOptionFunction(rangeFilterOptions -> setRangeFilterOptions(rangeFilterOptions));
    setRangeFilterStateFunction(rangeFilterState -> setRangeFilterState(rangeFilterState));
  }

  /**
   * レンジフィルターUIの設定関数をセットする
   * 
   * @param rangeFilterUiSetter
   */
  protected void setRangeFilterUiFunction(UnaryOperator<ChartRangeFilterUi> rangeFilterUiSetter) {
    this.rangeFilterUiSetter = rangeFilterUiSetter;
  }

  /**
   * レンジ幅の諸元数値をセットする
   * 
   * @param miniRangeWidth
   * @param maxRangeWidth
   * @param defaultRangeWidth
   */
  protected void setRangeValues(int miniRangeWidth, int maxRangeWidth, int defaultRangeWidth) {
    this.miniRangeWidth = miniRangeWidth;
    this.maxRangeWidth = maxRangeWidth;
    this.defaultRangeWidth = defaultRangeWidth;
  }

  /**
   * レンジフィルターオプションの設定
   * 
   * @param rangeFilterOptions
   * @return
   */
  private ChartRangeFilterOptions
      setRangeFilterOptions(ChartRangeFilterOptions rangeFilterOptions) {
    rangeFilterOptions =
        Optional.ofNullable(rangeFilterOptions).orElse(ChartRangeFilterOptions.create());
    rangeFilterOptions.setFilterColumnIndex(0);
    rangeFilterOptions.setUi(getRangeFilterUi());
    return rangeFilterOptions;
  }

  /**
   * レンジフィルターUI定義体を取得する
   * 
   * @return
   */
  private ChartRangeFilterUi getRangeFilterUi() {
    ChartRangeFilterUi filterUi = ChartRangeFilterUi.create();
    filterUi.setMinRangeSize(miniRangeWidth);
    filterUi = rangeFilterUiSetter.apply(filterUi);
    return filterUi;
  }

  /**
   * レンジフィルターステートの設定
   * 
   * @param rangeFilterState
   * @return
   */
  private ChartRangeFilterState setRangeFilterState(ChartRangeFilterState rangeFilterState) {
    rangeFilterState = Optional.ofNullable(rangeFilterState).orElse(ChartRangeFilterState.create());
    rangeFilterState.setRange(getStateRange());
    return rangeFilterState;
  }

  /**
   * グラフのレンジ幅定義体を生成する
   * 
   * @param startRange
   * @param endRange
   * @return
   */
  private ChartRangeFilterStateRange getStateRange() {
    ChartRangeFilterStateRange stateRange = ChartRangeFilterStateRange.create();
    stateRange.setStart(maxRangeWidth - defaultRangeWidth);
    stateRange.setEnd(maxRangeWidth);
    return stateRange;
  }

}
