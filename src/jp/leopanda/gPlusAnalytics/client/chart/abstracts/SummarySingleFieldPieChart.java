package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.SortColumn;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * ソースアイテムリストの１項目を集計表示する円グラフを作成するための総称クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class SummarySingleFieldPieChart<I extends PlusItem>
    extends SimpleChart<I, PieChartOptions> {
  private String columnTitle = "カラムタイトル";
  private String numberColumnTitle = "数値タイトル";
  private Function<I, String> summaryFieldSetter;

  /**
   * コンストラクタ
   */
  public SummarySingleFieldPieChart(Function<I, String> summaryFieldSetter) {
    super(ChartType.PIE, ChartPackage.CORECHART);
    setSummaryField(summaryFieldSetter);
    setChartOptionsFunction(chartOptions -> getChartOptions(chartOptions));
    setDataTableFunction(dataTable -> getDataTable(dataTable));
  }

  /**
   * 集計フィールドを設定する
   * 
   * @param summaryFieldSetter
   */
  private void setSummaryField(Function<I, String> summaryFieldSetter) {
    this.summaryFieldSetter = summaryFieldSetter;
  }

  /*
   * グラフのオプションを作成する
   */
  private PieChartOptions getChartOptions(PieChartOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    return chartOptions;
  }

  /*
   * グラフに表示するデータをセットする
   */
  private DataTable getDataTable(DataTable dataTable) {
    Map<String, Integer> summaryMap = summarySourceData(summaryFieldSetter);
    dataTable = setDataTable(summaryMap, dataTable);
    dataTable.sort(setFirstColumToDesc());
    return dataTable;
  }

  /**
   * 第1カラムを降順にセット
   * 
   * @return
   */
  private SortColumn setFirstColumToDesc() {
    SortColumn sortColum = SortColumn.create(1);
    sortColum.setDesc(true);
    return sortColum;
  }

  /**
   * ソースデータを同一項目毎に集計する
   * 
   * @return
   */
  private Map<String, Integer> summarySourceData(Function<I, String> summaryFieldSetter) {
    Map<String, Integer> summaryMap = new HashMap<String, Integer>();
    getSourceData().forEach(item -> {
      String summaryField = Optional.ofNullable(summaryFieldSetter.apply(item)).orElse("該当なし");
      if (summaryMap.keySet().contains(summaryField)) {
        summaryMap.put(summaryField, summaryMap.get(summaryField) + 1);
      } else {
        summaryMap.put(summaryField, 1);
      }
    });
    return summaryMap;
  }

  /**
   * データテーブルをセットする
   * 
   * @param summaryMap
   * @return
   */
  private DataTable setDataTable(Map<String, Integer> summaryMap, DataTable dataTable) {
    dataTable.addColumn(ColumnType.STRING, columnTitle);
    dataTable.addColumn(ColumnType.NUMBER, numberColumnTitle);
    summaryMap.forEach((k,v) -> dataTable.addRow(k, v));
    return dataTable;
  }
}
