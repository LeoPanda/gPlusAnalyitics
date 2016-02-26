package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * メニューに載せるチャートの共通パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class OnMenuPanel extends VerticalPanel {
	private ChartInfo chartInfo;

	public void setMenuInfo(ChartInfo chartInfo) {
		this.chartInfo = chartInfo;
		setWidth(String.valueOf(chartInfo.width) + "px");
	}
	
	protected int getChartWidth(){
		return this.chartInfo.width;
	}
	protected int getChartHeight(){
		return this.chartInfo.height;
	}
	protected String getChartTitle(){
		return this.chartInfo.title;
	}

}
