package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.List;
import java.util.Optional;

import com.google.gwt.user.client.Window;

import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

/**
 * google+アイテムデータをクリック時popup表示するcellTable
 * 
 * @author LeoPanda
 *
 */
public abstract class ClickablePlusItemTable<I extends PlusItem> extends SimpleCellTablePanel<I> {

  private boolean excludeSelectionChanged = false; // デフォルトの行選択アクションを一時的にスキップさせる場合にtrueをセット
  protected ItemClickListener<I> itemClickListener;// 表示アイテムに対して発生させたいイベントのリスナー

  public void addItemClickListener(ItemClickListener<I> listener) {
    itemClickListener = listener;
  }

  /**
   * コンストラクタ
   * 
   * @param items
   *          表示するアイテムデータ
   */
  public ClickablePlusItemTable(List<I> items) {
    super(items);
    this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
  }

  /**
   * デフォルトの行選択アクションを一時的に無効にする
   */
  public void disableOnSelectEventTemporally() {
    excludeSelectionChanged = true;
  }

  /*
   * デフォルトの行選択アクション 行がクリックされたらアイテムの詳細画面を表示する
   */
  protected void setSelectionChangeHandler() {
    this.selectionChangeHandler = event -> {
      Optional<I> selected = Optional.ofNullable(selectionModel.getSelectedObject());
      selected.ifPresent(selectedObject -> {
        selectionModel.setSelected(selectedObject, false);
        openWindowIfEnabled(selectedObject);
      });
    };
  }

  /**
   * 使用不可フラグが立っていなければ選択されたオブジェクトをウィンドウに表示する
   * @param selectedObject
   */
  private void openWindowIfEnabled(I selectedObject) {
    if (excludeSelectionChanged) {
      excludeSelectionChanged = false;
    } else {
      Window.open(selectedObject.getUrl(), "detail", FixedString.WINDOW_OPTION.getValue());
    }
  }

}
