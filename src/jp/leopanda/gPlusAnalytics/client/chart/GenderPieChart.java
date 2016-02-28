package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.abstracts.AggregatePieChart;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * ユーザーの性別集計円グラフを作成する
 * 
 * @author LeoPanda
 */
public class GenderPieChart extends AggregatePieChart<PlusPeople> {

  /*
   * コンストラクタ
   */
  public GenderPieChart() {
    super(Global.getPlusOners());

  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#getTargetField
   * (jp.leopanda.gPlusAnalytics.dataObject.PlusItem)
   */
  @Override
  protected String getTargetField(PlusPeople item) {
    return item.getGender();
  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#setFieldNameMap ()
   */
  @Override
  protected void setFieldAliasMap() {

  }

}
