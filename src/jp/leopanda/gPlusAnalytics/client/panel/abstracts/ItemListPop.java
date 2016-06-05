package jp.leopanda.gPlusAnalytics.client.panel.abstracts;


import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 特定条件でフィルターされたアイテムデータのリストをポップアップ画面に表示する
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemListPop<I extends PlusItem, T extends SimpleCellTable<I>, C> extends
    PopupPanel {

  private VerticalPanel innerPanel = new VerticalPanel();// ポップアップ画面の内枠


  /**
   * コンストラクタ
   * 
   * @param titleName 画面のタイトル
   * @param pageSize　リストの最大行数
   * @param itemTable 表示するアイテムテーブル
   * @param comparator フィルターの比較対象値
   * @param filter 　　フィルター比較演算関数
   */
  public ItemListPop(String titleName, int pageSize, T itemTable, C comparator,
      ItemFilter<I, C> filter) {
    this.setPopupPosition(200, 50);
    this.setWidth(itemTable.getWidth());
    innerPanel.add(makeCloseButton());
    innerPanel.add(makeItemList(titleName, pageSize, itemTable, comparator, filter));
    this.add(innerPanel);
  }

  /**
   * ポップアップウィンドウを閉じる
   * 
   * @return ポップアップ画面オブジェクト
   */
  public void closePop() {
    this.hide();
    this.clear();
  }

  /*
   * アイテムリストの作成
   */
  private SimpleItemListPanel<I, T> makeItemList(String titleName, int pageSize, T itemTable,
      C comparator, ItemFilter<I, C> filter) {
    SimpleItemListPanel<I, T> itemPanel =
        new SimpleItemListPanel<I, T>(titleName, pageSize, itemTable) {};
    filter.doFilter(comparator, itemTable);
    itemPanel.setDisplayCounter();
    return itemPanel;
  }

  /*
   * 閉じるボタンの作成
   */
  private Button makeCloseButton() {
    Button closeButton = new Button("x");
    closeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        closePop();
      }
    });
    return closeButton;
  }

}
