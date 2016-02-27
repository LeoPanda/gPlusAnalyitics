package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * ユーザ別＋１フィルター機能付きアクテビティ一覧
 * 
 * @author LeoPanda
 *
 */
public class FilterableActivityListPanel
		extends
			FilterableItemListPanel<PlusActivity, ActivityTable> {
	List<PlusActivity> originalItemList = new ArrayList<PlusActivity>();
	String stackName = "";
	Button resetButton;

	/**
	 * コンストラクタ
	 * 
	 * @param items
	 * @param pageSize
	 */
	public FilterableActivityListPanel(List<PlusActivity> items,
			String titleName, int pageSize, ActivityTable itemTable) {
		super(items, titleName, pageSize, itemTable);
		copyActivityList(items, originalItemList);
		resetButton = getResetButton();
	}
	/**
	 * +1erでフィルター
	 * 
	 * @param plusOnerId
	 */
	public void doPlusOnerFilter(String plusOnerId, String displayName) {
		new ItemFilter<PlusActivity, String>(itemList) {
			@Override
			public boolean compare(PlusActivity item, String comparator) {
				return item.getPlusOnerIds().contains(comparator);
			}
		}.doFilter(plusOnerId, itemTable);
		copyActivityList(itemTable.getDisplayList(), itemList);
		setTitleLine(displayName);
		spaceOfafterTitle.add(resetButton);
		clearFilterInput();
		pageStart = 0;
	}
	/**
	 * タイトル行をコントロールする
	 * 
	 * @param displayName
	 */
	private void setTitleLine(String displayName) {
		if (displayName == null) {
			stackName = "";
			setOriginalTitle();
		} else if(!stackName.contains(displayName)){
			stackName += stackName.length() == 0 ? "" : "と";
			stackName += displayName;
			setAlternateTitle(stackName + "の+1");
		}
	}
	/**
	 * アクティビティリストをコピーする
	 * 
	 * @param sourceList
	 * @return
	 */
	private void copyActivityList(List<PlusActivity> sourceList,
			List<PlusActivity> targetList) {
		targetList.clear();
		for (PlusActivity item : sourceList) {
			targetList.add(item);
		}
	}
	/**
	 * リセットボタンを作成する
	 * 
	 * @return
	 */
	private Button getResetButton() {
		Button resetButton = new Button("Reset");
		resetButton.setStyleName(CssStyle.ResetButton.getValue());
		resetButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				copyActivityList(originalItemList, itemList);
				doPlusOnerFilter(null, null);
				spaceOfafterTitle.clear();
				pageStart = 0;
			}
		});
		return resetButton;
	}

}
