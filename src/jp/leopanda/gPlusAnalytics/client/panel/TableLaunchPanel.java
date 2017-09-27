package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.PagedItemListPanel;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 一覧表表示パネル
 * 
 * @author LeoPanda
 *
 */
public class TableLaunchPanel extends HorizontalPanel {
  FilterableSourceItems sourceItems;
  ActivityTablePanel activityTable;
  PlusOnersTablePanel plusOnersTable;
  PagedItemListPanel<PlusActivity, ActivityTablePanel> activityTablePanel;
  PagedItemListPanel<PlusPeople, PlusOnersTablePanel> plusOnersTablePanel;
  ItemClickListener<PlusItem> itemClickListener;

  /**
   * コンストラクタ
   */
  public TableLaunchPanel(FilterableSourceItems sourceItems) {
    this.sourceItems = sourceItems;
    if ((sourceItems.getActivities() == null) || (sourceItems.getPlusOners() == null)) {
      this.add(new Label("メンテナンスニューからデータストアの初期ロードを行ってください。"));
    } else {
      setUpTables();

      this.add(plusOnersTablePanel);
      this.add(new HTML(FixedString.BLANK_CELL.getValue()));
      this.add(activityTablePanel);
    }
  }

  /**
   * テーブル上でのフィルターイベントを拾うリスナーを設定する
   * 
   * @param filterEventListener
   */
  public void addItemEventListener(ItemClickListener<PlusItem> itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  /**
   * アイテムリストをロードし直す。
   * @param activityItems
   * @param plusOners
   */
  public void reloadItems() {
    activityTable.reLoadItems(sourceItems.getActivities());
    activityTablePanel.setDisplayCounter();
    plusOnersTable.reLoadItems(sourceItems.getPlusOners());
    plusOnersTablePanel.setDisplayCounter();
  }

  /*
   * 一覧表テーブルと表示パネルを新規作成する
   */
  private void setUpTables() {
    activityTable = new ActivityTablePanel(sourceItems.getActivities());
    plusOnersTable = new PlusOnersTablePanel(sourceItems.getPlusOners());
    
    activityTable.addItemClickListener(new ItemClickListener<PlusActivity>() {
      @Override
      public void onClick(PlusActivity item) {
        itemClickListener.onClick(item);
      }
    });
    plusOnersTable.addItemClickListener(new ItemClickListener<PlusPeople>() {
      @Override
      public void onClick(PlusPeople item) {
        itemClickListener.onClick(item);
      }
    });

    activityTablePanel = new PagedItemListPanel<PlusActivity, ActivityTablePanel>("アクティビティ一覧", 7,
        activityTable) {
    };
    plusOnersTablePanel = new PagedItemListPanel<PlusPeople, PlusOnersTablePanel>("+1ユーザー一覧", 10,
        plusOnersTable) {
    };
  }
}
