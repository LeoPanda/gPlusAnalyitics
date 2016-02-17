/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.Global;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * 一覧表表示パネル
 * @author LeoPanda
 *
 */
public class ListTablePanel extends HorizontalPanel {
	/**
 * 
 */
	public ListTablePanel() {
		this.add(new ActivityListPane(Global.getGoogleUserId()));
		this.add(new HTML("<br/>&nbsp;&nbsp;&nbsp;<br/>"));
		this.add(new PlusOnersListPane(Global.getGoogleUserId()));
	}
}
