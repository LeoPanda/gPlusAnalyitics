package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityCalendarChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityColumnChart;
import jp.leopanda.gPlusAnalytics.client.chart.NumOfPlusOnePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PlusOnersPieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PostCirclePieChart;
import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
	private HorizontalPanel chartPanel;

	private Map<ChartOnMenu, Widget> charts = new HashMap<ChartOnMenu, Widget>(); // チャートのインスタンス
	private List<ChartOnMenu> onMenuChart = new ArrayList<ChartOnMenu>(); // 現在表示中のチャート

	private final int CHARTPANEL_MAXCOLUMNS = 2; // チャートパネルの最大カラム数
	private int chartPanelColumns = 0; // チャートパネルの現在カラム数

	/**
	 * コンストラクタ
	 */
	public ChartMenuPanel() {
		this.setWidth("1600px");
		this.add(getLinkPanel());
		this.add(getChartPanel());
	}
	/**
	 * リンク表示用パネルの作成
	 * 
	 * @return
	 */
	private VerticalPanel getLinkPanel() {
		if (linkPanel == null) {
			linkPanel = new VerticalPanel();
			for (ChartOnMenu chart : ChartOnMenu.values()) {
				linkPanel.add(new HTML("<br/>"));
				Anchor link = new Anchor(chart.name);
				addClickHandler(link, chart);
				linkPanel.add(link);
			}
		}
		return linkPanel;
	}
	/**
	 * チャート表示用パネルの作成
	 * 
	 * @return
	 */
	private HorizontalPanel getChartPanel() {
		if (chartPanel == null) {
			chartPanel = new HorizontalPanel();
		}
		return chartPanel;
	}

	/**
	 * アンカークリックハンドラの追加
	 */
	private void addClickHandler(Anchor link, final ChartOnMenu chart) {
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Global.getActivityItems() == null
						|| Global.getPlusOners() == null) {
					Window.alert("データの読み込みが終わるまでしばらくお待ち下さい。");
				} else {
					addCharrtToPanel(chart);
				}
			}

		});

	}
	/**
	 * チャートをパネルに追加する
	 * 
	 * @param chart
	 */
	private void addCharrtToPanel(ChartOnMenu chart) {
		if (onMenuChart.contains(chart)) {
			return;
		}
		if (chartPanelColumns + chart.occupiedColum > CHARTPANEL_MAXCOLUMNS) {
			onMenuChart.clear();
			chartPanel.clear();
			chartPanelColumns = 0;
		}
		onMenuChart.add(chart);
		chartPanel.add(getChartInstance(chart));
		chartPanelColumns += chart.occupiedColum;
	}
	/**
	 * チャートのインスタンスを取得する
	 * 
	 * @param chart
	 * @return
	 */
	private Widget getChartInstance(ChartOnMenu chart) {
		if (charts.get(chart) == null) {
			charts.put(chart, getNewWidget(chart));
		}
		return charts.get(chart);
	}
	/**
	 * チャートを生成する
	 * 
	 * @param chart
	 * @return
	 */
	private Widget getNewWidget(ChartOnMenu chart) {
		Widget newChart = null;
		switch (chart) {
			case ACTIVIY_COLUMN :
				newChart = new ActivityColumnChart(chart);
				break;

			case ACTIVITY_CALENDAR :
				newChart = new ActivityCalendarChart(chart);
				break;

			case NUM_OF_PLUSONE :
				newChart = new NumOfPlusOnePieChart(chart);
				break;

			case PLUSONRES_PIE :
				newChart = new PlusOnersPieChart(chart);
				break;

			case POSTCIRCLE_PIE :
				newChart = new PostCirclePieChart(chart);
				break;

//			case GENDER_PIE :
//				newChart = new GenderPieChart(chart);
//				break;
//
//			case LANGUAGE_PIE :
//				newChart = new LanguagePieChart(chart);
//				break;

			default :
				break;
		}
		return newChart;
	}

}
