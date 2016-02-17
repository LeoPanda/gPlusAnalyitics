package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
/**
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class NumOfPlusOnePieChart extends SimpleChart<PieChartOptions> {
	private int totalPlusOne = 0;
	/**
	 * コンストラクタ
	 */
	public NumOfPlusOnePieChart(ChartOnMenu enums) {
		super(ChartType.PIE,ChartPackage.CORECHART,enums);
	}
	protected void afterDrawChart() {
		add(new Label("総+1数:"
				+ NumberFormat.getDecimalFormat().format(totalPlusOne)));
	};
	/**
	 * グラフのオプションを作成する
	 * 
	 * @param chartArea
	 * @return
	 */
	protected PieChartOptions getChartOptions() {
		super.getChartOptions();
		chartOptions.setTitle(chartTitle);
		chartOptions.setLegend(Legend.create(LegendPosition.TOP));
		return chartOptions;
	}

	/**
	 * グラフに表示するデータをセットする
	 * 
	 * @return
	 */
	DataTable getDataTable() {
		int first = 0, lowMiddle = 0, highMiddle = 0, high = 0;
		for (PlusActivity activity : Global.getActivityItems()) {
			first += activity.getFirstLookers();
			lowMiddle += activity.getLowMiddleLookers();
			highMiddle += activity.getHighMiddleLookers();
			high += activity.getHighLookers();
			totalPlusOne += (first + lowMiddle + highMiddle + high);
		}
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "+1分布");
		dataTable.addColumn(ColumnType.NUMBER, "ユーザー数");
		dataTable.addRow(Distribution.FIRST_LOOKER.getName(), first);
		dataTable.addRow(Distribution.LOW_MIDDLE_LOOKER.getName(), lowMiddle);
		dataTable.addRow(Distribution.HIGH_MIDDLE_LOOKER.getName(), highMiddle);
		dataTable.addRow(Distribution.HIGH_LOOKER.getName(), high);
		return dataTable;
	}
}
