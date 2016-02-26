package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityCalendarChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityColumnChart;
import jp.leopanda.gPlusAnalytics.client.chart.NumOfPlusOnePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.OnMenuPanel;
import jp.leopanda.gPlusAnalytics.client.chart.PlusOnersPieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PostCirclePieChart;
import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;

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

	private Map<ChartInfo, OnMenuPanel> charts = new HashMap<ChartInfo, OnMenuPanel>(); // チャートのインスタンス
	private List<ChartInfo> onMenuChart = new ArrayList<ChartInfo>(); // 現在表示中のチャート

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
			for (ChartInfo chart : ChartInfo.values()) {
				linkPanel.add(new HTML("<br/>"));
				Anchor link = new Anchor(chart.title);
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
	private void addClickHandler(Anchor link, final ChartInfo chartInfo) {
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Global.getActivityItems() == null
						|| Global.getPlusOners() == null) {
					Window.alert("データの読み込みが終わるまでしばらくお待ち下さい。");
				} else {
					addCharrtToPanel(chartInfo);
				}
			}

		});

	}
	/**
	 * チャートをパネルに追加する
	 * 
	 * @param chartInfo
	 */
	private void addCharrtToPanel(ChartInfo chartInfo) {
		if (onMenuChart.contains(chartInfo)) {
			return;
		}
		if (chartPanelColumns + chartInfo.occupiedColum > CHARTPANEL_MAXCOLUMNS) {
			onMenuChart.clear();
			chartPanel.clear();
			chartPanelColumns = 0;
		}
		onMenuChart.add(chartInfo);
		chartPanel.add(getChartInstance(chartInfo));
		chartPanelColumns += chartInfo.occupiedColum;
	}
	/**
	 * チャートのインスタンスを取得する
	 * 
	 * @param chartInfo
	 * @return
	 */
	private OnMenuPanel getChartInstance(ChartInfo chartInfo) {
		if (charts.get(chartInfo) == null) {
			charts.put(chartInfo, getNewWidget(chartInfo));
		}
		return charts.get(chartInfo);
	}
	/**
	 * チャートを生成する
	 * 
	 * @param chartInfo
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private OnMenuPanel getNewWidget(ChartInfo chartInfo) {
		OnMenuPanel newChart = null;
		switch (chartInfo) {
			case ACTIVIY_COLUMN :
				newChart = new ActivityColumnChart();
				break;

			case ACTIVITY_CALENDAR :
				newChart = new ActivityCalendarChart();
				break;

			case NUM_OF_PLUSONE :
				newChart = new NumOfPlusOnePieChart();
				break;

			case PLUSONRES_PIE :
				newChart = new PlusOnersPieChart();
				break;

			case POSTCIRCLE_PIE :
				newChart = new PostCirclePieChart();
				break;
//			case GENDER_PIE :
//				newChart = new GenderPieChart();
//				break;
//
//			case LANGUAGE_PIE :
//				newChart = new LanguagePieChart();
//				break;

			default :
				break;
		}
		newChart.setMenuInfo(chartInfo);
		return newChart;
	}

}
