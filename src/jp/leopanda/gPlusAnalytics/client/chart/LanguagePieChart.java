/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * ユーザーの使用言語集計円グラフを作成する
 * 
 * @author LeoPanda
 *
 */
public class LanguagePieChart extends AggregatePieChart<PlusPeople> {

	/**
	 * コンストラクタ
	 * 
	 * @param enums
	 */
	public LanguagePieChart() {
		super(Global.getPlusOners());

	}

	/*
	 * @see
	 * jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#getTargetField
	 * (jp.leopanda.gPlusAnalytics.dataObject.PlusItem)
	 */
	@Override
	String getTargetField(PlusPeople item) {
		return item.language;
	}

	/*
	 * @see
	 * jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#setFieldNameMap
	 * ()
	 */
	@Override
	void setFieldAliasMap() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
