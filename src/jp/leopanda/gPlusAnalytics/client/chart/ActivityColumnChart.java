package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.user.client.Window;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.ColumnChartOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.Bar;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.TextPosition;
import com.googlecode.gwt.charts.client.options.VAxis;
/**
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class ActivityColumnChart extends ColumnChartRangeFilterdChart<ColumnChartOptions> {
	private Map<Integer, String> activityUrls = new HashMap<Integer, String>();
	private Bar columnBar; //カラムのバー設定
	Logger logger = Logger.getLogger("ActivityColumnChart");

	/**
	 * コンストラクタ
	 */
	public ActivityColumnChart() {
		super(ChartType.COLUMN);
		logger.log(Level.INFO,"ActivityColumnChart.constractor:");
		columnBar = Bar.create();
		columnBar.setGroupWidth("98%");
	}
	/* 
	 * チャートの生成
	 * @see jp.leopanda.gPlusAnalytics.client.chart.SimpleChart#getChart(com.googlecode.gwt.charts.client.ChartType)
	 */
	@Override
	protected ChartWrapper<ColumnChartOptions> getChart(ChartType chartType) {
		logger.log(Level.INFO,"ActivityColumnChart.getChart.start");
		chart = super.getChart(chartType);
		chart.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Integer selectedRow = (int) chart.getDataTable()
						.getValueNumber(
								chart.getSelection().get(0).getRow(),
								0);
				Window.open(activityUrls.get(selectedRow), "",
						WindowOption.ItemDetail.getValue());
			}
		});
		logger.log(Level.INFO,"ActivityColumnChart.getChart.end");
		return chart;
	}
	/* 
	 * チャートのオプションを生成する
	 * @see jp.leopanda.gPlusAnalytics.client.chart.SimpleChart#getChartOptions()
	 */
	@Override
	protected ColumnChartOptions getChartOptions() {
		logger.log(Level.INFO,"ActivityColumnChart.getChartOptions.start");

		chartOptions =  super.getChartOptions();
		chartOptions.setTitle(getChartTitle());
		chartOptions.setLegend(Legend.create(LegendPosition.TOP));
		HAxis haxis = HAxis.create("アクテビティ");
		haxis.setTextPosition(TextPosition.NONE);
		chartOptions.setHAxis(haxis);
		chartOptions.setVAxes(VAxis.create("+1数"));
		//バーの設定
		chartOptions.setBar(columnBar);
		//積み上げカラムに設定
		chartOptions.setIsStacked(true);
		logger.log(Level.INFO,"ActivityColumnChart.getChartOptions.end");
		return chartOptions;

	}
	/*
	 * レンジフィルターの設定 
	 * @see jp.leopanda.gPlusAnalytics.client.chart.ColumnChartRangeFilterdChart#setRangeFilter()
	 */
	@Override
	protected void setRangeFilter() {
		logger.log(Level.INFO,"ActivityColumnChart.setRangeFilter.start");
		MAX_RANGE = Global.getActivityItems().size();
		MINI_RANGE_WIDTH = 5;
		RANGE_WIDTH = 10;
		super.setRangeFilter();
		//日付でフィルター
		filterOptions.setFilterColumnIndex(0); 
		//フィルターチャートの外形設定
		filterChartOptions.setHeight(100);
		HAxis haxis = HAxis.create();
		haxis.setTextPosition(TextPosition.NONE);
		filterChartOptions.setHAxis(haxis);
		filterChartOptions.setIsStacked(true);
		//フィルターチャートのカラムバーをチャートと同一に設定
		filterChartOptions.setBar(columnBar);
		logger.log(Level.INFO,"ActivityColumnChart.setRangeFilter.end");
	}
	/**
	 * グラフに表示するデータをセットする
	 * 
	 * @return
	 */
	DataTable getDataTable() {
		logger.log(Level.INFO,"ActivityColumnChart.getDataTable.start");
		List<PlusActivity> activities = Global.getActivityItems();
		Collections.sort(activities, new Comparator<PlusActivity>() {
			@Override
			public int compare(PlusActivity o1, PlusActivity o2) {
				return o1.getPublished().compareTo(o2.getPublished());
			}
		});
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.NUMBER, "No.");
		dataTable.addColumn(ColumnType.NUMBER,
				Distribution.FIRST_LOOKER.getName());
		dataTable.addColumn(ColumnType.NUMBER,
				Distribution.LOW_MIDDLE_LOOKER.getName());
		dataTable.addColumn(ColumnType.NUMBER,
				Distribution.HIGH_MIDDLE_LOOKER.getName());
		dataTable.addColumn(ColumnType.NUMBER,
				Distribution.HIGH_LOOKER.getName());
		dataTable.addRows(activities.size() + 1);
		int row = 1;
		for (PlusActivity activity : activities) {
			activityUrls.put(row, activity.getUrl());
			dataTable.setValue(row, 0, row);
			dataTable.setValue(row, 1, activity.getFirstLookers());
			dataTable.setValue(row, 2, activity.getLowMiddleLookers());
			dataTable.setValue(row, 3, activity.getHighMiddleLookers());
			dataTable.setValue(row, 4, activity.getHighLookers());
			row += 1;
		}
		logger.log(Level.INFO,"ActivityColumnChart.getDataTable.end");
		return dataTable;
	}

}
