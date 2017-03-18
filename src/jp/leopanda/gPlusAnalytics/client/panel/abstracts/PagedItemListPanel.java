package jp.leopanda.gPlusAnalytics.client.panel.abstracts;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Google+ API アイテムデータの一覧表示パネル ページコントロール機能付き
 * 
 * @author LeoPanda
 *
 */
public abstract class PagedItemListPanel<I, T extends SimpleCellTable<I>> extends
    SimpleItemListPanel<I, T> {

  protected int pageStart = 0;// 現在表示しているページ先頭行の位置
  private PageControlLine pageControlLine; //ページコントロール行
  
  /**
   * ページコントロール行の中央部分のフリースペースを取得する
   * @return
   */
  public HorizontalPanel getPageControlFreeSpace(){
    return pageControlLine.getFreeSpace();
  }

  /**
   * コンストラクタ
   * 
   * @param items
   *          表示するデータのリスト
   * @param titleName
   *          パネルのタイトル名
   * @param pageSize
   *          １ページに表示する行数
   * @param itemTable
   *          一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
   */
  public PagedItemListPanel(String titleName, int pageSize, T itemTable) {
    super(pageSize, itemTable);
    // タイトル行の追加
    addTitleLine(titleName);
    // ページコントロール行の追加
    pageControlLine = makePageControlLine();
    this.add(pageControlLine);
    // アイテム表示テーブルの追加
    addItemTable(itemTable);
    // カラムソート時に先頭行をリセット
    addSortEventHandler();
  }

  /*
   * ページコントロール行の生成
   */
  private PageControlLine makePageControlLine() {
    return new PageControlLine(itemTable.getWidth()) {
      @Override
      public void onFirstPageButtonClick(ClickEvent event) {
        // 先頭ページボタン
        pageStart = 0;
        itemTable.setPageStart(0);
      }

      @Override
      public void onLastPageButtonClick(ClickEvent event) {
        // 最終ページボタン
        pageStart = itemTable.getDisplayList().size() - pageSize;
        itemTable.setPageStart(pageStart);
      }

      @Override
      public void onPrevPageButtonClick(ClickEvent event) {
        // 前ページボタン
        pageStart -= pageSize;
        pageStart = pageStart < 0 ? 0 : pageStart;
        itemTable.setPageStart(pageStart);
      }

      @Override
      public void onNextPageButtonClick(ClickEvent event) {
        // 次ページボタン
        pageStart += pageSize;
        int lastPage = itemTable.getDisplayList().size();
        pageStart = pageStart + 1 > lastPage ? pageStart - pageSize : pageStart;
        itemTable.setPageStart(pageStart);
      }

    };
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
