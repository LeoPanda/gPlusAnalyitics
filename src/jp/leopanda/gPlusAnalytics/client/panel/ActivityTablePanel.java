package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ClickablePlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ImageColumn;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル
 * 
 * @author LeoPanda
 *
 */
public class ActivityTablePanel extends ClickablePlusItemTable<PlusActivity> {
  /**
   * コンストラクタ
   */
  public ActivityTablePanel(List<PlusActivity> items) {
    super(items);
  }

  TextColumn<PlusActivity> publishedColumn; // 投稿日
  TextColumn<PlusActivity> titleColumn; // タイトル
  ImageColumn<PlusActivity> imageColumn; // 投稿写真
  TextColumn<PlusActivity> accessColumn;// 投稿先
  ButtonColumn<PlusActivity> filterButton; // +1er数フィルターボタン

  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    // 投稿日
    publishedColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return Formatter.getYYMDString(item.getPublished());
      }
    };

    // タイトル
    titleColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return item.getTitle();
      }
    };
    titleColumn.setCellStyleNames(MyStyle.TABLE_TEXT.getStyle());

    // 投稿写真
    imageColumn = new ImageColumn<PlusActivity>("80", null) {
      @Override
      public SafeHtml getValue(PlusActivity item) {
        return getImageTagFromUrl(item.getFirstAttachmentImageUrl());
      }
    };
    imageColumn.setCellStyleNames(MyStyle.TABLE_PHOTO.getStyle());

    // 投稿先
    accessColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return item.getAccessDescription();
      }
    };
    accessColumn.setCellStyleNames(MyStyle.TABLE_TEXT.getStyle());

    // +1er数フィルターボタン
    filterButton = new ButtonColumn<PlusActivity>() {
      @Override
      public void addClickEvent(int index, PlusActivity item) {
        disableOnSelectEventTemporally();
        itemClickListener.onClick(item);
      }

      @Override
      public String getValue(PlusActivity item) {
        return String.valueOf(item.getNumOfPlusOners());
      }
    };

    // カラム表示リストに登録
    this.columSets.add(newColumnSet("投稿日", publishedColumn, 100, null));
    this.columSets.add(newColumnSet("タイトル", titleColumn, 400, null));
    this.columSets.add(newColumnSet("写真", imageColumn, 100, null));
    this.columSets.add(newColumnSet("投稿先", accessColumn, 100, null));
    this.columSets.add(newColumnSet("+1", filterButton, 30, HasHorizontalAlignment.ALIGN_RIGHT));
  }

  /**
   * カラムのソート条件をセットする
   */
  @Override
  protected void setSortHandler() {
    // 投稿日でソート
    publishedColumn.setSortable(true);
    sortHandler.setComparator(publishedColumn,Comparator.comparing(PlusActivity::getPublished));
//        (x, y) -> x.getPublished().compareTo(y.getPublished()));
    // 投稿先でソート
    accessColumn.setSortable(true);
    sortHandler.setComparator(accessColumn,Comparator.comparing(PlusActivity::getAccessDescription));
//        (x, y) -> x.getAccessDescription().compareTo(y.getAccessDescription()));
    // +1でソート
    filterButton.setSortable(true);
    sortHandler.setComparator(filterButton,Comparator.comparing(PlusActivity::getNumOfPlusOners));
//        (x, y) -> x.getNumOfPlusOners() - y.getNumOfPlusOners());

  }
}
