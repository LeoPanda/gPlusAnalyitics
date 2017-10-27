package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.panel.parts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.parts.ImageColumn;
import jp.leopanda.gPlusAnalytics.client.panel.parts.PlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.parts.StringColumn;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル
 * 
 * @author LeoPanda
 *
 */
public class ActivityTable extends PlusItemTable<PlusActivity> {

  StringColumn<PlusActivity> publishedColumn; // 投稿日
  StringColumn<PlusActivity> titleColumn; // タイトル
  ImageColumn<PlusActivity> imageColumn; // 投稿写真
  StringColumn<PlusActivity> accessColumn;// 投稿先
  ButtonColumn<PlusActivity> filterButton; // +1er数フィルターボタン

  /**
   * コンストラクタ
   */
  public ActivityTable(List<PlusActivity> items) {
    super(items);
    setSrotHandler(handler -> setSortRequirement(handler));
  }

  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    publishedColumn =
        new StringColumn<PlusActivity>(item -> Formatter.getYYMDString(item.getPublished()));

    titleColumn = new StringColumn<PlusActivity>(item -> item.getTitle());
    titleColumn.setCellStyleNames(MyStyle.TABLE_TEXT.getStyle());

    imageColumn =
        new ImageColumn<PlusActivity>("80", null, item -> item.getFirstAttachmentImageUrl());
    imageColumn.setCellStyleNames(MyStyle.TABLE_PHOTO.getStyle());

    accessColumn = new StringColumn<PlusActivity>(item -> item.getAccessDescription());
    accessColumn.setCellStyleNames(MyStyle.TABLE_TEXT.getStyle());

    filterButton = new ButtonColumn<PlusActivity>(item -> String.valueOf(item.getNumOfPlusOners()));
    filterButton.addClickEvent((index, item) -> setButtonClickEvent(item));
    filterButton.setCellStyleNames(MyStyle.FILTER_LABEL.getStyle());

    // カラム表示リストに登録
    addColumnSet("投稿日", publishedColumn, 100, null);
    addColumnSet("タイトル", titleColumn, 400, null);
    addColumnSet("写真", imageColumn, 100, null);
    addColumnSet("投稿先", accessColumn, 100, null);
    addColumnSet("+1", filterButton, 30, HasHorizontalAlignment.ALIGN_RIGHT);
  }

  /**
   * カラムのソート条件をセットする
   */
  private void setSortRequirement(ListHandler<PlusActivity> sortHandler) {
    // 投稿日でソート
    publishedColumn.setSortable(true);
    sortHandler.setComparator(publishedColumn,
        Comparator.comparing(PlusActivity::getPublished));
    // 投稿先でソート
    accessColumn.setSortable(true);
    sortHandler.setComparator(accessColumn,
        Comparator.comparing(PlusActivity::getAccessDescription));
    // +1でソート
    filterButton.setSortable(true);
    sortHandler.setComparator(filterButton,
        Comparator.comparing(PlusActivity::getNumOfPlusOners));
  }
}
