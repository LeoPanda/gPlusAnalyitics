package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.SelectionChangeEvent;

import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

/**
 * google+アイテムデータをクリック時popup表示するcellTable
 * 
 * @author LeoPanda
 *
 */
public abstract class ClickablePlusItemTable<I extends PlusItem> extends SimpleCellTable<I> {

  private boolean excludeSelectionChanged = false; // デフォルトの行選択アクションを一時的にスキップさせる場合にtrueをセット
  protected ItemClickListener<I> itemClickListener;// 表示アイテムに対して発生させたいイベントのリスナー

  public void addItemClickListener(ItemClickListener<I> listener) {
    itemClickListener = listener;
  }

  /**
   * コンストラクタ
   * 
   * @param items 表示するアイテムデータ
   */
  public ClickablePlusItemTable(List<I> items) {
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
  protected void setSelectionChangeHandler() {
    this.selectionChangeHandler = new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        I selected = selectionModel.getSelectedObject();
        if (selected != null) {
          if (excludeSelectionChanged) {
            excludeSelectionChanged = false;
          } else {
            Window.open(selected.getUrl(), "detail", FixedString.WINDOW_OPTION.getValue());
          }
        }
      }
    };
  }

}
