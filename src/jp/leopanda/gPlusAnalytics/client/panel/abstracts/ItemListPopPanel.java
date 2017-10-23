package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.List;
import java.util.function.Predicate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.util.ItemFilter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * 特定条件でフィルターされたアイテムデータのリストをポップアップ画面に表示する
 * 
 * @author LeoPanda
 *
 */
public class ItemListPopPanel<I extends PlusItem, T extends SimpleCellTablePanel<I>> extends
    PopupPanel {

  private VerticalPanel innerPanel = new VerticalPanel();// ポップアップ画面の内枠
  private SimpleItemListPanel<I, T> itemPanel;

  /**
   * コンストラクタ
   * 
   * @param titleName
   *          画面のタイトル
   * @param pageSize
   *          リストの最大行数
   * @param itemTable
   *          表示するアイテムテーブル
   * @param predicate
   *          フィルター条件式
   */
  public ItemListPopPanel(String titleName, int pageSize, T itemTable) {
    super(false, true);
    this.setPopupPosition(200, 50);
    this.setWidth(itemTable.getWidth());
    innerPanel.add(makeCloseButton());
    innerPanel.add(makeItemListTable(titleName, pageSize, itemTable));
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

  /**
   * アイテムリストの作成
   * 
   * @param titleName
   * @param pageSize
   * @param itemTable
   * @return
   */
  private SimpleItemListPanel<I, T> makeItemListTable(String titleName, int pageSize, T itemTable) {

    itemPanel = new SimpleItemListPanel<I, T>(titleName, pageSize, itemTable) {};
    return itemPanel;

  }

  /**
   * フィルター条件を設定してポップアップを表示する
   * 
   * @param filterPredicate
   */
  public void show(Predicate<I> filterPredicate) {
    doFilterItemPanel(filterPredicate);
    itemPanel.itemTable.setPageStart(0);
    itemPanel.setDisplayCounter();
    super.show();
  }

  /**
   * アイテムパネルをフィルターする
   * 
   * @param itemTable
   */
  private void doFilterItemPanel(Predicate<I> predicate) {
    List<I> displayList = itemPanel.itemTable.getDisplayList();
    List<I> filteredList = new ItemFilter<I>(displayList) {}.doFilter(predicate);
    displayList.clear();
    filteredList.forEach(item -> displayList.add(item));
  }

  /**
   * 閉じるボタンの作成
   * 
   * @return
   */
  private Button makeCloseButton() {
    Button closeButton = new Button("x");
    closeButton.addClickHandler(e -> closePop());
    return closeButton;
  }

}
