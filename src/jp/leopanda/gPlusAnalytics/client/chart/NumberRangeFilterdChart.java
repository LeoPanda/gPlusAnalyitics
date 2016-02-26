package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.filter.NumberRangeFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.NumberRangeFilterState;
import com.googlecode.gwt.charts.client.options.Options;

/**
 * 数値フィルター付きチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class NumberRangeFilterdChart<O extends Options>
		extends
			FilterdChart<O, NumberRangeFilterOptions, NumberRangeFilterState> {

	/**
	 * コンストラクタ
	 * 
	 * @param chartType
	 * @param enums
	 */
	public NumberRangeFilterdChart(ChartType chartType) {
		super(chartType, ControlType.NUMBER_RANGE_FILTER);
	}

}
