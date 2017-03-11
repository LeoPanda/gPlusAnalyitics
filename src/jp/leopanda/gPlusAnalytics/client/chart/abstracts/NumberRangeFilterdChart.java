package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.filter.NumberRangeFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.NumberRangeFilterState;
import com.googlecode.gwt.charts.client.options.Options;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * 数値フィルター付きチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class NumberRangeFilterdChart<I extends PlusItem, O extends Options> extends
    FilterdChart<I, O, NumberRangeFilterOptions, NumberRangeFilterState> {

  /**
   * コンストラクタ
   * 
   * @param chartType
   *          チャートタイプ
   */
  public NumberRangeFilterdChart(ChartType chartType) {
    super(chartType, ControlType.NUMBER_RANGE_FILTER);
  }

}
