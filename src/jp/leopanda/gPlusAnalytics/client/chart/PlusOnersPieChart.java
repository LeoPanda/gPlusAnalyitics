package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SummryDistributionValuesPieChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;


/**
 * +1ユーザーの累積＋１数分布状況をグラフ化するための総称クラス
 * 
 * @author LeoPanda
 *
 */
public class PlusOnersPieChart extends SummryDistributionValuesPieChart<PlusPeople> {
  private int totalPlusOners = 0;

  /**
   * コンストラクタ
   */
  public PlusOnersPieChart() {
    super("+1分布", "ユーザー数");
    setSummaryFunction(values -> summaryDistributions(values));
  }

  /**
   * 数値集計
   * 
   * @param values
   */
  private void summaryDistributions(DistributionValues values) {
    getSourceData().forEach(plusOner -> {
      totalPlusOners += 1;
      int plusOne = plusOner.getNumOfPlusOne();
      if (plusOne >= Distribution.HIGH_LOOKER.threshold) {
        values.high += 1;
      } else if (plusOne >= Distribution.HIGH_MIDDLE_LOOKER.threshold) {
        values.highMiddle += 1;
      } else if (plusOne >= Distribution.LOW_MIDDLE_LOOKER.threshold) {
        values.lowMiddle += 1;
      } else {
        values.first += 1;
      }
    });
    setSummaryLabel("総ユーザー数", totalPlusOners);
  }
}
