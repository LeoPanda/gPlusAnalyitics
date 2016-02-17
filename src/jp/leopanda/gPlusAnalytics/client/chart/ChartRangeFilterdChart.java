package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterState;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterStateRange;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterUi;
import com.googlecode.gwt.charts.client.options.Options;
/**
 * チャートレンジフィルター付きチャート
 * 
 * @author LeoPanda
 *
 * @param <O>
 */
public abstract class ChartRangeFilterdChart<O extends Options>
		extends
			FilterdChart<O, ChartRangeFilterOptions, ChartRangeFilterState> {
	protected int MINI_RANGE_WIDTH; // レンジフィルターの最小レンジ幅
	protected int MAX_RANGE; // レンジフィルターの最大値
	protected int RANGE_WIDTH; // レンジフィルターのデフォルト幅
	protected ChartRangeFilterUi filterUi;// レンジフィルターチャートのUIオプション
	protected ChartRangeFilterStateRange stateRange; // レンジフィルターのレンジ幅定義体

	/**
	 * コンストラクタ
	 * 
	 * @param chartType
	 * @param enums
	 */
	public ChartRangeFilterdChart(ChartType chartType, ChartOnMenu enums) {
		super(chartType, ControlType.CHART_RANGE_FILTER, enums);
	}
	/**
	 * レンジフィルターの初期設定
	 */
	@Override
	protected void setRangeFilter() {
		filterOptions = getfilterOptions();
		filterOptions.setUi(getFilterUi());
		filterUi = getFilterUi();
		filterState = getFilterState();
		filterState.setRange(setStateRange(MAX_RANGE-RANGE_WIDTH,MAX_RANGE));
	}
	/**
	 * レンジフィルターチャートのUIオプションをカラムチャート用にセットする
	 * 
	 * @return
	 */
	private ChartRangeFilterUi getFilterUi() {
		if (filterUi == null) {
			filterUi = ChartRangeFilterUi.create();
			filterUi.setMinRangeSize(MINI_RANGE_WIDTH);
		}
		return filterUi;
	}
	/**
	 * グラフのレンジ幅を設定する
	 * 
	 * @return
	 */
	private ChartRangeFilterStateRange setStateRange(int startRange,int endRange) {
		stateRange = ChartRangeFilterStateRange.create();
		stateRange.setStart(startRange);
		stateRange.setEnd(endRange);
		return stateRange;
	}


}
