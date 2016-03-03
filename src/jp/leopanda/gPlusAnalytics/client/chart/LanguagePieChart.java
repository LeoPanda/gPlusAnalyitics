package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.AggregatePieChart;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * ユーザーの使用言語集計円グラフを作成する
 * 
 * @author LeoPanda
 *
 */
public class LanguagePieChart extends AggregatePieChart<PlusPeople> {

  /*
   * コンストラクタ
   * 
   */
  public LanguagePieChart() {
    super();

  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#getTargetField
   * (jp.leopanda.gPlusAnalytics.dataObject.PlusItem)
   */
  @Override
  protected String getTargetField(PlusPeople item) {
    return item.language;
  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#setFieldNameMap ()
   */
  @Override
  protected void setFieldAliasMap() {
  }

}
