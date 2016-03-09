package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.SelectionChangeEvent;

import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemEventListener;

/**
 * google+アイテムデータ表示するcellTable
 * 
 * @author LeoPanda
 *
 */
public abstract class PlusItemTable<I extends PlusItem> extends SimpleCellTable<I> {

  private boolean excludeSelectionChanged = false; // デフォルトの行選択アクションを一時的にスキップさせる場合にtrueをセット
  protected ItemEventListener<I> itemEventListener;// 表示アイテムに対して発生させたいイベントのリスナー

  public void addItemEventListener(ItemEventListener<I> listener) {
    itemEventListener = listener;
  }

  /**
   * コンストラクタ
   * 
   * @param items 表示するアイテムデータ
   */
  public PlusItemTable(List<I> items) {
    super(items);
  }
  /**
   * デフォルトの行選択アクションを一時的に無効にする
   */
  public void disableOnselectEventTemporally() {
    excludeSelectionChanged = true;
  }

  /*
   * デフォルトの行選択アクション 行がクリックされたらアイテムの詳細画面を表示する
   */
  @Override
  protected void setSelectionChangeHandler() {
    this.selectionChangeHandler = new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        I selected = selectionModel.getSelectedObject();
        if (selected != null) {
          if (excludeSelectionChanged) {
            excludeSelectionChanged = false;
          } else {
            Window.open(selected.getUrl(), "detail", WindowOption.ITEM_DETAIL.getValue());
          }
        }
      }
    };
  }

}
