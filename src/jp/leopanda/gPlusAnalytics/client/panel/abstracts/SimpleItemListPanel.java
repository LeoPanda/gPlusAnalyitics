package jp.leopanda.gPlusAnalytics.client.panel.abstracts;


import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Google+ API　アイテムデータの一覧表示パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class SimpleItemListPanel<I extends PlusItem, T extends SimpleCellTable<I>> extends
    VerticalPanel {

  protected SimpleCellTable<I> itemTable;// 表示するアイテムデータ用の表
  private Label titleLabel = new Label();// パネルのタイトル名を表示するラベル
  private String originalTitle;// 元のタイトル名
  private Label lineCountLabel = new Label();// アイテムの総数を表示するラベル
  protected int pageSize = 0;// 表示するアイテムの行数
  // フリースペース
  protected HorizontalPanel spaceOfafterTitle = new HorizontalPanel();// タイトル行の後

  /**
   * コンストラクタ
   * 
   * @param items 表示するデータのリスト
   * @param titleName パネルのタイトル名
   * @param pageSize １ページに表示する行数
   * @param itemTable 一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
   */
  public SimpleItemListPanel(String titleName, int pageSize, T itemTable) {
    this.itemTable = itemTable;
    this.pageSize = pageSize;
//    this.itemList = items;
    // タイトル行の追加
    addTitleLine(titleName);
    // アイテム表示テーブルの追加
    addItemTable(itemTable);
  }
  /**
   * コンストラクタ(初期表示なし)
   * 
   * @param items 表示するデータのリスト
   * @param pageSize １ページに表示する行数
   * @param itemTable 一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
   */
  public SimpleItemListPanel(int pageSize, T itemTable) {
    this.itemTable = itemTable;
    this.pageSize = pageSize;
//    this.itemList = items;
  }

  /*
   * タイトル行を追加する
   */
  protected void addTitleLine(String titleName) {
    this.originalTitle = titleName;
    HorizontalPanel headerLine = makeTitleLine();
    this.add(headerLine);
    this.add(spaceOfafterTitle);
  }

  /*
   * タイトル行の生成
   */
  private HorizontalPanel makeTitleLine() {
    HorizontalPanel headerLine = new HorizontalPanel();
    headerLine.setWidth(itemTable.getWidth());
    setOriginalTitle();
    titleLabel.addStyleName(CssStyle.LABEL_TITLE.getName());
    lineCountLabel.addStyleName(CssStyle.LABEL_COUNT.getName());
    headerLine.add(titleLabel);
    headerLine.add(lineCountLabel);
    return headerLine;
  }


  /**
   * オリジナルのタイトルを表示する
   */
  public void setOriginalTitle() {
    titleLabel.setText(originalTitle);
    setDisplayCounter();
  }

  /**
   * 代替のタイトルを表示する
   * 
   * @param title 表示するタイトル
   */
  public void setAlternateTitle(String title) {
    titleLabel.setText(title);
    setDisplayCounter();
  }

  /**
   * 選択されたデータの合計行数を表示する
   */
  public void setDisplayCounter() {
    lineCountLabel.setText("(" + String.valueOf(itemTable.getDisplayList().size()) + "件)");
  }

  /*
   * アイテム表示テーブルを追加する
   */
  protected void addItemTable(T itemTable) {
    itemTable.setPageSize(this.pageSize);
    this.add(itemTable);
    // 表示幅の設定
    this.setWidth(itemTable.getWidth());
  }

}
