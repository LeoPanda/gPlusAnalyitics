package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.FilterableItemListPanel;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ItemFilter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * ユーザ別＋１フィルター機能付きアクテビティ一覧
 * 
 * @author LeoPanda
 *
 */
public class FilterableActivityListPanel extends
    FilterableItemListPanel<PlusActivity, ActivityTable> {
  List<PlusActivity> originalItemList = new ArrayList<PlusActivity>();
  String stackName = "";
  Button resetButton;

  /**
   * コンストラクタ
   * 
   * @param items 一覧に表示するデータのリスト
   * @param titleName 一覧表のタイトル
   * @param pageSize １ページの表示行数
   * @param itemTable 一覧表示する表オブジェクト
   */
  public FilterableActivityListPanel(List<PlusActivity> items, String titleName, int pageSize,
      ActivityTable itemTable) {
    super(items, titleName, pageSize, itemTable);
    copyActivityList(items, originalItemList);
    resetButton = getResetButton();
  }

  /**
   * +1erでフィルター
   * 
   * @param plusOnerId フィルター対象の+1ユーザーのID
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

  /*
   * タイトル行をコントロールする
   */
  private void setTitleLine(String displayName) {
    if (displayName == null) {
      stackName = "";
      setOriginalTitle();
    } else if (!stackName.contains(displayName)) {
      stackName += stackName.length() == 0 ? "" : "と";
      stackName += displayName;
      setAlternateTitle(stackName + "の+1");
    }
  }

  /*
   * アクティビティリストをコピーする
   */
  private void copyActivityList(List<PlusActivity> sourceList, List<PlusActivity> targetList) {
    targetList.clear();
    for (PlusActivity item : sourceList) {
      targetList.add(item);
    }
  }

  /*
   * リセットボタンを作成する
   */
  private Button getResetButton() {
    Button resetButton = new Button("Reset");
    resetButton.setStyleName(CssStyle.BUTTON_RESET.getName());
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
