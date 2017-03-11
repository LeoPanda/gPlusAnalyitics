package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.SortColumn;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public abstract class AggregatePieChart<I extends PlusItem> extends SimpleChart<I,PieChartOptions> {
  private Map<String, Integer> aggregateMap = new HashMap<String, Integer>();
  protected Map<String, String> fieldAliasMap = new HashMap<String, String>();
  protected String columnTitle = "カラムタイトル";
  protected String numberColumnTitle = "数値タイトル";

  /**
   * コンストラクタ
   */
  public AggregatePieChart() {
    super(ChartType.PIE, ChartPackage.CORECHART);
    setFieldAliasMap();
  }

  /*
   * グラフのオプションを作成する
   */
  protected PieChartOptions getChartOptions() {
    super.getChartOptions();
    chartOptions.setTitle(getChartTitle());
    return chartOptions;
  }

  /*
   * グラフに表示するデータをセットする
   */
  @Override
  protected DataTable getDataTable() {
    for (I item : sourceData) {
      String targetField = getTargetField(item);
      if (targetField == null) {
        targetField = "null";
      }
      if (aggregateMap.get(targetField) == null) {
        aggregateMap.put(targetField, 1);
      } else {
        aggregateMap.put(targetField, aggregateMap.get(targetField) + 1);
      }
    }
    dataTable = super.getDataTable();
    dataTable.addColumn(ColumnType.STRING, columnTitle);
    dataTable.addColumn(ColumnType.NUMBER, numberColumnTitle);
    for (String field : aggregateMap.keySet()) {
      String title = fieldAliasMap.get(field) != null ? fieldAliasMap.get(field) : field;
      dataTable.addRow(title, aggregateMap.get(field));
    }
    aggregateMap.clear();
    // 降順にソート
    SortColumn sortColum = SortColumn.create(1);
    sortColum.setDesc(true);
    dataTable.sort(sortColum);
    return dataTable;
  }

  /*
   * 集計したい項目を選ぶ
   * 
   */
  protected abstract String getTargetField(I item);

  /**
   * 集計フィールドの表示名を変更したい場合は、ここでfieldAliasMapを登録する fieldAliasMap(フィールドの実値,変更後の値)
   * 
   */
  protected abstract void setFieldAliasMap();
}
