package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.Optional;
import java.util.function.UnaryOperator;

import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.controls.ControlOptions;
import com.googlecode.gwt.charts.client.controls.ControlState;
import com.googlecode.gwt.charts.client.controls.ControlType;
import com.googlecode.gwt.charts.client.controls.ControlWrapper;
import com.googlecode.gwt.charts.client.controls.Dashboard;
import com.googlecode.gwt.charts.client.options.Options;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * フィルター付きチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class FilterdChart<I extends PlusItem, O extends Options, CO extends ControlOptions<?>, CS extends ControlState>
    extends ChartBasePanel<I, O> {
  private Dashboard dashboard;
  private ControlWrapper<CO, CS> controlFilter;
  private ControlType controlType;
  private CO controlFilterOptions;
  private CS controlFilterState;
  private UnaryOperator<CO> controlFilterOptionSetter;
  private UnaryOperator<CS> controlFilterStateSetter;

  /**
   * コンストラクタ <br/>
   * 
   * @param chartType チャートタイプ
   * @param controlTyle チャートコントロールタイプ
   */
  public FilterdChart(ChartType chartType, ControlType controlTyle) {
    super(chartType, ChartPackage.CONTROLS);
    this.controlType = controlTyle;
  }

  /**
   * レンジフィルターオプション設定関数をセットする
   * 
   * @param rangeFilterOptionSetter
   * @param rangeFilterStateSetter
   */
  protected void setRangeFilterOptionFunction(
      UnaryOperator<CO> controlFilterOptionSetter) {
    this.controlFilterOptionSetter = controlFilterOptionSetter;
  }

  /**
   * レンジフィルターオプションステート設定関数をセットする
   * 
   * @param controlFilterStateSetter
   */
  protected void setRangeFilterStateFunction(UnaryOperator<CS> controlFilterStateSetter) {
    this.controlFilterStateSetter = controlFilterStateSetter;
  }

  /*
   * チャートの描画処理
   */
  @Override
  void runDraw(ChartWrapper<O> chart, DataTable dataTable) {
    dashboard = getDashboardWidget();
    controlFilter = geControlFilter(controlType);
    // 要素を画面へ追加
    add(dashboard);
    add(chart);
    add(controlFilter);
    // グラフ描画
    dashboard.bind(controlFilter, chart);
    dashboard.draw(dataTable);
  }

  /**
   * ダッシュボードの作成
   */
  private Dashboard getDashboardWidget() {
    return Optional.ofNullable(dashboard).orElse(new Dashboard());
  }

  /**
   * コントロールフィルターの作成
   * 
   * @param controlType
   * @return
   */
  private ControlWrapper<CO, CS> geControlFilter(ControlType controlType) {
    controlFilter = Optional.ofNullable(controlFilter).orElse(new ControlWrapper<CO, CS>());
    controlFilter.setControlType(controlType);
    controlFilter.setOptions(controlFilterOptionSetter.apply(controlFilterOptions));
    controlFilter.setState(controlFilterStateSetter.apply(controlFilterState));
    return controlFilter;
  }

}
