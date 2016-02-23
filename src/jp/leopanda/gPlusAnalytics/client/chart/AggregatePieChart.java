package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

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
public abstract class AggregatePieChart<I extends PlusItem> extends SimpleChart<PieChartOptions> {
	private List<I> items;
	private Map<String, Integer> aggregateMap = new HashMap<String, Integer>();
	protected Map<String,String> fieldNameMap = new HashMap<String, String>();
	protected String columnTitle = "カラムタイトル";
	protected String numberColumnTitle ="数値タイトル";
	/**
	 * コンストラクタ
	 */
	public AggregatePieChart(List<I> items,ChartOnMenu enums) {
		super(ChartType.PIE,ChartPackage.CORECHART,enums);
		setFieldNameMap();
		this.items = items;
	}
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
		for (I item : items) {
			String targetField = getTargetField(item);
			if(targetField == null){
				targetField = "null";
			}
			if(aggregateMap.get(targetField) == null){
				aggregateMap.put(targetField,1);
			}else{
				aggregateMap.put(targetField, aggregateMap.get(targetField)+1);
			}
		}
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, columnTitle);
		dataTable.addColumn(ColumnType.NUMBER, numberColumnTitle);
		for(String field:aggregateMap.keySet()){
			String title = fieldNameMap.get(field) != null ? fieldNameMap.get(field) : field;
			dataTable.addRow(title,aggregateMap.get(field));
			}
		return dataTable;
	}
	/**
	 * 集計したい項目を選ぶ
	 * @param item
	 * @return
	 */
	abstract String getTargetField(I item);
	/**
	 * 集計フィールドの名称を変更したい場合は、ここでfieldNameMapを登録する
	 * fieldNameMap(フィールドの実値,変更後の値)
	 * 
	 */
	abstract void setFieldNameMap();
}
