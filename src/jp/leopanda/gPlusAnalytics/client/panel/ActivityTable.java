package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.Formatter;
import jp.leopanda.gPlusAnalytics.client.HtmlBuilder;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.PlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SafeHtmlColumn;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル
 * 
 * @author LeoPanda
 *
 */
public class ActivityTable extends PlusItemTable<PlusActivity> {
  /**
   * コンストラクタ
   */
  public ActivityTable(List<PlusActivity> items) {
    super(items);
  }

  TextColumn<PlusActivity> publishedColumn; // 投稿日
  TextColumn<PlusActivity> titleColumn; // タイトル
  SafeHtmlColumn<PlusActivity> imageColumn; // 投稿写真
  TextColumn<PlusActivity> accessColumn;// 投稿先
  ButtonColumn<PlusActivity> filterButton; // +1er数フィルターボタン
  Formatter formatter = new Formatter(); //項目フォーマット関数



  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    // 投稿日
    publishedColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return formatter.getYYMMDDString(item.getPublished());
      }
    };
    // タイトル
    titleColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return item.getTitle();
      }
    };
    // 投稿写真
    imageColumn = new SafeHtmlColumn<PlusActivity>() {
      @Override
      public SafeHtml getValue(PlusActivity item) {
        HtmlBuilder builder = new HtmlBuilder();
        if (item.getAttachmentImageUrls() != null) {
          builder.appendActivityTumbnailImg(item.getAttachmentImageUrls().get(0));
        }
        return builder.getSafeHtml();
      }
    };
    // 投稿先
    accessColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        String result = item.getAccessDescription();
        return result;
      }
    };
    // +1er数フィルターボタン
    filterButton = new ButtonColumn<PlusActivity>() {
      @Override
      public void addClickEvent(int index, PlusActivity item) {
        disableOnselectEventTemporally();
        itemEventListener.onEvent(item);
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
    sortHandler.setComparator(publishedColumn, new Comparator<PlusActivity>() {
      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o1.getPublished().compareTo(o2.getPublished());
      }
    });
    // 投稿先でソート
    accessColumn.setSortable(true);
    sortHandler.setComparator(accessColumn, new Comparator<PlusActivity>() {
      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o1.getAccessDescription().compareTo(o2.getAccessDescription());
      }
    });
    // +1でソート
    filterButton.setSortable(true);
    sortHandler.setComparator(filterButton, new Comparator<PlusActivity>() {
      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o1.getNumOfPlusOners() - o2.getNumOfPlusOners();
      }
    });

  }
}
