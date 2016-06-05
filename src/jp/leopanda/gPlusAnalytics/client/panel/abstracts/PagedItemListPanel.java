package jp.leopanda.gPlusAnalytics.client.panel.abstracts;


import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Google+ API　アイテムデータの一覧表示パネル　ページコントロール機能付き
 * 
 * @author LeoPanda
 *
 */
public abstract class PagedItemListPanel<I extends PlusItem, T extends SimpleCellTable<I>> extends
    SimpleItemListPanel<I,T> {

  protected int pageStart = 0;// 現在表示しているページ先頭行の位置
  private Button firstPageButton = new Button("◀◀");
  private Button lastPageButton = new Button("▶▶");
  private Button prevPageButton = new Button("◀");
  private Button nextPageButton = new Button("▶");
  // フリースペース
  protected HorizontalPanel spaceOfPageControl = new HorizontalPanel();// ページコントロールの間

  /**
   * コンストラクタ
   * 
   * @param items 表示するデータのリスト
   * @param titleName パネルのタイトル名
   * @param pageSize １ページに表示する行数
   * @param itemTable 一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
   */
  public PagedItemListPanel(String titleName, int pageSize, T itemTable) {
    super(pageSize,itemTable);
    // タイトル行の追加
    addTitleLine(titleName);
    // ページコントロール行の追加
    HorizontalPanel pageControlLine = makePageControlLine();
    this.add(pageControlLine);
    // アイテム表示テーブルの追加
    addItemTable(itemTable);
    // カラムソート時に先頭行をリセット
    addSortEventHandler();
  }

  /*
   * ページコントロール行の生成
   */
  private HorizontalPanel makePageControlLine() {
    addButtonClickHandlers();
    firstPageButton.addStyleName(CssStyle.BUTTON_PAGE.getName());
    prevPageButton.addStyleName(CssStyle.BUTTON_PAGE.getName());
    nextPageButton.addStyleName(CssStyle.BUTTON_PAGE.getName());
    lastPageButton.addStyleName(CssStyle.BUTTON_PAGE.getName());
    HorizontalPanel pageControlLine = new HorizontalPanel();
    pageControlLine.setWidth(itemTable.getWidth());
    HorizontalPanel leftSide = new HorizontalPanel();
    HorizontalPanel rightSide = new HorizontalPanel();
    leftSide.add(firstPageButton);
    leftSide.add(prevPageButton);
    rightSide.add(nextPageButton);
    rightSide.add(lastPageButton);
    pageControlLine.add(leftSide);
    pageControlLine.setHorizontalAlignment(ALIGN_LEFT);
    pageControlLine.add(spaceOfPageControl);
    pageControlLine.setHorizontalAlignment(ALIGN_RIGHT);
    pageControlLine.add(rightSide);
    return pageControlLine;
  }

  /*
   * 各ページコントロールボタンへクリックハンドラを追加する
   */
  private void addButtonClickHandlers() {
    // 先頭ページボタン
    firstPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        pageStart = 0;
        itemTable.setPageStart(0);
      }
    });
    // 最終ページボタン
    lastPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        pageStart = itemTable.getDisplayList().size() - pageSize;
        itemTable.setPageStart(pageStart);
      }
    });
    // 前ページボタン
    prevPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        pageStart -= pageSize;
        pageStart = pageStart < 0 ? 0 : pageStart;

        itemTable.setPageStart(pageStart);
      }
    });
    // 次ページボタン
    nextPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        pageStart += pageSize;
        int lastPage = itemTable.getDisplayList().size();
        pageStart = pageStart + 1 > lastPage ? pageStart - pageSize : pageStart;
        itemTable.setPageStart(pageStart);
      }
    });
  }

  /*
   * セルテーブルのソートイベント発生時にテーブルの表示先頭行をリセット
   */
  private void addSortEventHandler() {
    itemTable.sortEventHander = new ColumnSortEvent.Handler() {
      @Override
      public void onColumnSort(ColumnSortEvent event) {
        pageStart = 0;
        itemTable.setPageStart(0);
      }
    };
  }
}
