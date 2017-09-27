package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SummryDistributionValuesPieChart;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class NumOfPlusOnePieChart extends SummryDistributionValuesPieChart<PlusActivity> {

  /**
   * コンストラクタ
   */
  public NumOfPlusOnePieChart() {
    super("+1分布","ユーザー数");
    setSummaryFunction(values -> summaryDistributions(values));
  }

  /**
   * 数値集計
   * @param values
   */
  private void summaryDistributions(DistributionValues values) {
    getSourceData().forEach(activity -> {
      values.first += activity.getFirstLookers();
      values.lowMiddle += activity.getLowMiddleLookers();
      values.highMiddle += activity.getHighMiddleLookers();
      values.high += activity.getHighLookers();
    });
    setSummaryLabel("総+1数:", (values.first + values.lowMiddle + values.highMiddle + values.high));
  }

}
