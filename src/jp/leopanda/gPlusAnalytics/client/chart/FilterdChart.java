package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;

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
public abstract class FilterdChart<O extends Options, CO extends ControlOptions<?>, CS extends ControlState>
		extends
			SimpleChart<O> {
	private Dashboard dashboard;
	private ControlWrapper<CO, CS> rangeFilter;
	protected CO filterOptions;
	protected CS filterState;
	private ControlType controlType;

	/**
	 * コンストラクタ ChartPackage.CONTROLS使用時にはChartype.CALENDARは使用できません。
	 * 
	 * @param chartType
	 * @param chartPackage
	 * @param enums
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
	/**
	 * チャートを描画する
	 */
	@Override
	protected void draw() {
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
	/**
	 * ダッシュボードの作成
	 * 
	 * @return
	 */
	private Dashboard getDashboardWidget() {
		if (dashboard == null) {
			dashboard = new Dashboard();
		}
		return dashboard;
	}
	/**
	 * レンジフィルターの作成
	 * 
	 * @return
	 */
	private ControlWrapper<CO, CS> getRangeFilter(ControlType controlType) {
		if (rangeFilter == null) {
			rangeFilter = new ControlWrapper<CO, CS>();
			rangeFilter.setControlType(controlType);
		}
		return rangeFilter;
	}
	/**
	 * レンジフィルターオプションの作成
	 * 
	 * @return
	 */
	abstract CO getfilterOptions();
	/**
	 * レンジフィルターステートの作成
	 * 
	 * @return
	 */
	abstract CS getFilterState();

	/**
	 * レンジフィルターの設定 getFilterOptions あるいは getFilterlStateを呼び出し
	 * レンジフィルターに必要なオプションとステートを設定する。
	 */
	abstract void setRangeFilter();

}
