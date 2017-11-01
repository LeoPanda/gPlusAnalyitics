package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.List;

import com.google.gwt.user.client.Window;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemClickListener;

/**
 * google+アイテムデータをクリック時popup表示するcellTable
 * 
 * @author LeoPanda
 *
 */
public abstract class PlusItemTable<I extends PlusItem> extends SimpleCellTable<I> {

  private boolean excludeSelectionChanged = false; // デフォルトの行選択アクションを一時的にスキップさせる場合にtrueをセット
  private ItemClickListener<I> itemClickListener;// 表示アイテムに対して発生させたいイベントのリスナー

 
  /**
   * コンストラクタ
   * 
   * @param items
   */
  public PlusItemTable(List<I> items) {
    super(items);
    setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
    setSelectionHandler(selectedItem -> openWindowIfEnabled(selectedItem));
  }
  /**
   * アイテムクリック時のリスナーを設定する
   * @param listener
   */
  public void addItemClickListener(ItemClickListener<I> listener) {
    itemClickListener = listener;
  }

  /**
   * アイテム表示行のボタンがクリックされた時のイベントを定義する
   * 呼び出されると、デフォルトの行選択アクションは一時的に無効になる
   */
  public void setButtonClickEvent(I item,FilterType filterType) {
    excludeSelectionChanged = true;
    itemClickListener.onClick(item,filterType);
  }

  
  /**
   * 使用不可フラグが立っていなければ選択されたオブジェクトをウィンドウに表示する
   * @param selectedItem
   */
  private void openWindowIfEnabled(I selectedItem) {
    if (excludeSelectionChanged) {
      excludeSelectionChanged = false;
    } else {
      Window.open(selectedItem.getUrl(), "detail", FixedString.WINDOW_OPTION.getValue());
    }
  }

}
