package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.corechart.ComboChartOptions;
import com.googlecode.gwt.charts.client.options.Options;
import com.googlecode.gwt.charts.client.options.SeriesType;

/**
 * レンジフィルターにカラムチャートを使用したチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class ColumnChartRangeFilterdChart<O extends Options>
		extends
			ChartRangeFilterdChart<O> {
	protected ComboChartOptions filterChartOptions; // レンジフィルターのチャートオプション
	/**
	 * カラムチャートをレンジフィルタとして搭載したチャート
	 * 
	 * @param chartType
	 * @param enums
	 */
	public ColumnChartRangeFilterdChart(ChartType chartType) {
		super(chartType);
	}
	/**
	 * レンジフィルターの初期設定
	 */
	@Override
	protected void setRangeFilter() {
		super.setRangeFilter();
		filterUi.setChartType(ChartType.COMBO);
		filterUi.setChartOptions(presetFilterChartOptions());
	}
	/**
	 * レンジフィルターのチャートオプションをカラムチャート用にセットする
	 * 
	 * @return
	 */
	private ComboChartOptions presetFilterChartOptions() {
		if (filterChartOptions == null) {
			filterChartOptions = ComboChartOptions.create();
			filterChartOptions.setWidth(getChartWidth());
			filterChartOptions.setSeriesType(SeriesType.BARS);
		}
		return filterChartOptions;
	}
}
