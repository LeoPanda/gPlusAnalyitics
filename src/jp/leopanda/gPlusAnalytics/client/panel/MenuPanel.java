package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.chart.ChartMenuPanel;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * メインメニュー
 * @author LeoPanda
 *
 */
public class MenuPanel extends TabPanel{

	/**
	 * コンストラクタ
	 */
	public MenuPanel() {
//		super(1.5, Unit.EM);
		this.add(new ListTablePanel(),"tables");
		this.add(new ChartMenuPanel(),"chart");
		this.add(new MaintenancePanel(),"maintenance");
		this.selectTab(0);
	}

}
