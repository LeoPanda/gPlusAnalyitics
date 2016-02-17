package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.enums.ChartOnMenu;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * メニューに載せるチャートの共通パネル
 * @author LeoPanda
 *
 */
public abstract class OnMenuPanel extends VerticalPanel {
	protected int chartWidth,chartHeight;
	protected String chartTitle;
	/**
	 * 
	 */
	OnMenuPanel(ChartOnMenu enums){
		this.chartWidth = enums.width;
		this.chartHeight = enums.height;
		this.chartTitle = enums.name;
		setWidth(String.valueOf(chartWidth) + "px");
	}

}
