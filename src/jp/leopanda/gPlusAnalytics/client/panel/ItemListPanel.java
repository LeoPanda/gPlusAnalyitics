package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;

import jp.leopanda.gPlusAnalytics.client.panel.parts.PageControlButtons;
import jp.leopanda.gPlusAnalytics.client.panel.parts.SimpleCellTable;
import jp.leopanda.gPlusAnalytics.client.panel.parts.SimpleItemListPanel;

/**
 * Google+ API アイテムデータの一覧表示パネル ページコントロール機能付き
 * 
 * @author LeoPanda
 *
 */
public class ItemListPanel<I, T extends SimpleCellTable<I>> extends
    SimpleItemListPanel<I, T> {

  private int pageStart = 0;// 現在表示しているページ先頭行の位置
  private PageControlButtons pageControlButtons; // ページコントロール行

  /**
   * ページコントロール行の中央部分のフリースペースを取得する
   * 
   * @return
   */
  public HorizontalPanel getPageControlFreeSpace() {
    return pageControlButtons.getFreeSpace();
  }

  /**
   * コンストラクタ
   * 
   * @param items 表示するデータのリスト
   * @param titleName パネルのタイトル名
   * @param pageSize １ページに表示する行数
   * @param itemTable 一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
   */
  public ItemListPanel(String titleName, int pageSize, T itemTable) {
    super(pageSize, itemTable);
    addTitleLine(titleName);
    pageControlButtons = makePageControlButtons();
    add(pageControlButtons);
    addItemTable(itemTable);
    getItemTable().setSortEventHandler(event -> {
      pageStart = 0;
      getItemTable().setPageStart(0);
    });
  }

  /*
   * ページコントロールボタン生成
   */
  private PageControlButtons makePageControlButtons() {
    PageControlButtons controlButtons = new PageControlButtons(getItemTable().getWidth());
    controlButtons.addFirstPageButtonClickHandler(event -> {
      pageStart = 0;
      getItemTable().setPageStart(0);
    });
    controlButtons.addlastPageButtonClickHandler(event -> {
      pageStart = getItemTable().getDisplayList().size() - pageSize;
      getItemTable().setPageStart(pageStart);
    });
    controlButtons.addPrevPageButtonClickHandler(event -> {
      pageStart -= pageSize;
      pageStart = pageStart < 0 ? 0 : pageStart;
      getItemTable().setPageStart(pageStart);
    });
    controlButtons.addNextPageButtonClickHandler(event -> {
      pageStart += pageSize;
      int lastPage = getItemTable().getDisplayList().size();
      pageStart = pageStart + 1 > lastPage ? pageStart - pageSize : pageStart;
      getItemTable().setPageStart(pageStart);
    });
    return controlButtons;
  }
}
