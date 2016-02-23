/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.chart;


import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * @author LeoPanda
 *
 */
public class PostCirclePieChart extends AggregatePieChart<PlusActivity> {

	/**
	 * 投稿先集計パイチャート
	 * 
	 * @param items
	 * @param enums
	 */
	public PostCirclePieChart(ChartOnMenu enums) {
		super(Global.getActivityItems(), enums);
	}

	/*
	 * @see
	 * jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#getTargetField
	 * (jp.leopanda.gPlusAnalytics.dataObject.PlusItem)
	 */
	@Override
	String getTargetField(PlusActivity item) {
		return item.getAccessDescription();
	}

	/*
	 * @see
	 * jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#setFieldNameMap
	 * ()
	 */
	@Override
	void setFieldNameMap() {
		fieldNameMap.put("Public", "コレクション");
	}

}
