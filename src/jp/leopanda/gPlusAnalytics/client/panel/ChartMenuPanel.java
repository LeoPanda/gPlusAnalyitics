package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ChartsOnMenu;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.Drawable;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * チャートメニュー
 * 
 * @author LeoPanda
 *
 */
public class ChartMenuPanel extends HorizontalPanel {

  private VerticalPanel linkPanel;
  private ChartPanel chartPanel;

  private FilterableSourceItems sourceItems;

  /**
   * コンストラクタ
   */
  public ChartMenuPanel(FilterableSourceItems sourceItems) {
    this.sourceItems = sourceItems;
    this.setWidth("1600px");
    this.add(getMenuLinkPanel());
    this.add(getChartPanel());
  }

  /**
   * インスタンス化されたチャートを再描画する
   */
  public void reDrawCharts() {
    chartPanel.reDrawCharts();
  }

  /**
   * メニューリンク表示用パネルの作成
   * 
   * @return
   */
  private VerticalPanel getMenuLinkPanel() {
    linkPanel = new VerticalPanel();
    ChartsOnMenu.forEach(chartOnPanel -> {
      linkPanel.add(new HTML(FixedString.BLANK_CELL.getValue()));
      linkPanel.add(getMenuAnchor(chartOnPanel.getTitle(), chartOnPanel));
    });
    return linkPanel;
  }

  /**
   * チャート表示パネルの作成
   * 
   * @return
   */
  private VerticalPanel getChartPanel() {
    this.chartPanel = new ChartPanel();
    VerticalPanel wrapPanel = new VerticalPanel();
    wrapPanel.add(chartPanel);
    return wrapPanel;
  }

  /**
   * メニューリンクアンカーの作成
   * 
   * @param title
   * @param chartOnMenu
   * @return
   */
  private Anchor getMenuAnchor(String title, ChartsOnMenu chartOnMenu) {
    Anchor anchor = new Anchor(title);
    anchor.addClickHandler(event -> chartPanel.launchChart(chartOnMenu));
    return anchor;
  }

  /**
   * チャート表示用パネル定義クラス
   * 
   * @author LeoPanda
   *
   */
  private class ChartPanel extends HorizontalPanel {
    private final int maxColumsNumber = 2;// パネルの最大カラム数
    private int ocupiedColumsNumber = 0;
    private List<ChartsOnMenu> launchedChart = new ArrayList<ChartsOnMenu>();

    /**
     * 表示されたチャートを再描画する
     */
    public void reDrawCharts() {
      launchedChart.forEach(chartOnMenu -> {
        Drawable<?> chart = chartOnMenu.getChart();
        chart.setSourceData(sourceItems.getItemList(chartOnMenu.getItemType()));
        chart.reDraw();
      });
    }

    /**
     * 指定されたチャートをパネルに搭載する
     * 
     * @param chartOnMenu
     */
    public void launchChart(ChartsOnMenu chartOnMenu) {
      if (launchedChart.contains(chartOnMenu)) {
        return;
      }
      ocupiedColumsNumber += chartOnMenu.getRequiredColumsNumber();
      if (ocupiedColumsNumber > maxColumsNumber) {
        ocupiedColumsNumber = chartOnMenu.getRequiredColumsNumber();
        resetChartPanel();
      }
      addChartAndDraw(chartOnMenu);
    }

    /**
     * パネル表示をリセットする
     */
    private void resetChartPanel() {
      launchedChart.forEach(launchedChart -> launchedChart.getChart().resetChart());
      launchedChart.clear();
      this.clear();
    }

    /**
     * パネルにチャートを追加する
     * 
     * @param chartOnMenu
     */
    private void addChartAndDraw(ChartsOnMenu chartOnMenu) {
      launchedChart.add(chartOnMenu);
      Drawable<?> chart = chartOnMenu.getChart();
      this.add((Widget) chart);
      chart.setSourceData(sourceItems.getItemList(chartOnMenu.getItemType()));
      chart.draw();
    }
  }

}
