package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.TableEventListener;

/**
 * アイテムリストテーブルにフィルター機能を追加する
 * 
 * @author LeoPanda
 *
 */
public abstract class FilterableItemListPanel<I extends PlusItem, T extends SimpleCellTable<I>>
    extends ItemListPanel<I, T> {
  TextBox filterInput = new TextBox();
  private String filterLog = new String(); // フィルターの履歴
  private Label filterLogPanel = new Label();
  private TableEventListener eventListener; // イベント発生通知

  public void addEventListener(TableEventListener eventListener) {
    this.eventListener = eventListener;
  }

  /**
   * コンストラクタ
   * 
   * @param itemList 表示するアイテムデータのリスト
   * @param titleName 一覧表のタイトル
   * @param pageSize 1ページに表示する行数
   * @param itemTable 表示に使用する表オブジェクト
   */
  public FilterableItemListPanel(List<I> itemList, String titleName, int pageSize, T itemTable) {
    super(itemList, titleName, pageSize, itemTable);
    addChangeHandler();
    spaceOfPageControl.add(filterInput);
    spaceOfPageControl.add(getResetButton());
    spaceOfafterTitle.add(filterLogPanel);
    filterLogPanel.setStyleName(CssStyle.LABEL_FILTER.getName());
  }


  /*
   * 入力フィールドにenter key検知ハンドラを設定する
   */
  private void addChangeHandler() {
    filterInput.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          doStringFilter(filterInput.getValue());;
        }
      }
    });
  }

  /*
   * 入力文字でフィルター
   */
  private void doStringFilter(String comparator) {
    doFilter(comparator, comparator, new ItemFilter<I, String>() {
      @Override
      public boolean compare(I item, String comparator) {
        return (item.getFilterSourceValue().toLowerCase().contains(comparator.toLowerCase()));
      }
    });
  }

  /*
   * リセットボタンを作成する
   */
  private Button getResetButton() {
    Button resetButton = new Button("X");
    resetButton.setStyleName(CssStyle.BUTTON_RESET.getName());
    resetButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        resetFilter();
        pageStart = 0;
      }
    });
    return resetButton;
  }

  /**
   * フィルターの実行
   * 
   * @param comparator 比較オブジェクトの値
   * @param filterName フィルタ履歴に表示する名称
   * @param itemFilter フィルタオブジェクト
   */
  public <C> void doFilter(C comparator, String filterName, ItemFilter<I, C> itemFilter) {
    if (filterLog.contains(filterName)) {
      return;
    }
    itemFilter.doFilter(comparator, itemTable);
    setDisplayCounter();
    clearFilterInput();
    displayFilterLog(filterName);
    pageStart = 0;
    eventListener.onFilter(filterLog);
  }

  /**
   * フィルターのリセット
   */
  public void resetFilter() {
    List<I> displayList = itemTable.getDisplayList();
    displayList.clear();
    for (I item : itemList) {
      displayList.add(item);
    }
    clearFilterInput();
    clearFilterLog();
    setOriginalTitle();
    eventListener.onFilter(filterLog);
  }

  /*
   * 入力フィールドのクリア
   */
  private void clearFilterInput() {
    filterInput.setText("");
  }

  /*
   * フィルター履歴を表示する
   */
  private void displayFilterLog(String filterName) {
    if (filterLog.length() == 0) {
      filterLog = "filtered:";
    } else {
      filterLog += ",";
    }
    filterLog += filterName;
    filterLogPanel.setText(filterLog);
  }

  /*
   * フィルター履歴のクリア
   */
  private void clearFilterLog() {
    filterLog = "";
    filterLogPanel.setText("");
  }
}
