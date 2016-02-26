package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

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
 * +1ユーザーの累積＋１数分布状況をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class PlusOnersPieChart extends SimpleChart<PieChartOptions> {
	private int totalPlusOners = 0;
	/**
	 * コンストラクタ
	 */
	public PlusOnersPieChart() {
		super(ChartType.PIE, ChartPackage.CORECHART);
	}
	@Override
	protected void afterDrawChart() {
		add(new Label("総ユーザー数:"
				+ NumberFormat.getDecimalFormat().format(totalPlusOners)));
	}
	/**
	 * グラフのオプションを作成する
	 * 
	 * @param chartArea
	 * @return
	 */
	protected PieChartOptions getChartOptions() {
		super.getChartOptions();
		chartOptions.setTitle(getChartTitle());
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
		for (PlusPeople plusOner : Global.getPlusOners()) {
			totalPlusOners += 1;
			int plusOne = plusOner.getNumOfPlusOne();
			if (plusOne >= Distribution.HIGH_LOOKER.getThreshold()) {
				high += 1;
			} else if (plusOne >= Distribution.HIGH_MIDDLE_LOOKER
					.getThreshold()) {
				highMiddle += 1;
			} else if (plusOne >= Distribution.LOW_MIDDLE_LOOKER.getThreshold()) {
				lowMiddle += 1;
			} else {
				first += 1;
			}
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
