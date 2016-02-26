/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.chart;


import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
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
	public PostCirclePieChart() {
		super(Global.getActivityItems());
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
	void setFieldAliasMap() {
	}

}
