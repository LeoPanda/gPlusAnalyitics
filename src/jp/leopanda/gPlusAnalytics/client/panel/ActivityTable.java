package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;

import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;
import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SafeHtmlColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SimpleCellTable;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル
 * 
 * @author LeoPanda
 *
 */
public class ActivityTable extends SimpleCellTable<PlusActivity> {
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
  TextColumn<PlusActivity> numOfPlusOneColumn; // +1の数

  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    // 投稿日
    publishedColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return DateTimeFormat.getFormat(DateFormat.YYMMDD.getValue()).format(item.getPublished());
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
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        if (item.getAttachmentImageUrls() != null) {
          String imageUrl = item.getAttachmentImageUrls().get(0);
          builder.appendHtmlConstant("<img height=\"80\" width =\"80\" src=\"")
              .appendEscaped(imageUrl).appendHtmlConstant("\">").appendHtmlConstant("<br/>");
        }
        return builder.toSafeHtml();
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
    // プラスワンユーザー数
    numOfPlusOneColumn = new TextColumn<PlusActivity>() {
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
    this.columSets.add(newColumnSet("+1", numOfPlusOneColumn, 30,
        HasHorizontalAlignment.ALIGN_RIGHT));
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
    numOfPlusOneColumn.setSortable(true);
    sortHandler.setComparator(numOfPlusOneColumn, new Comparator<PlusActivity>() {
      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o1.getNumOfPlusOners() - o2.getNumOfPlusOners();
      }
    });

    this.getColumnSortList().push(publishedColumn);

  }

  /*
   * 行がクリックされたらg+アクテビティ画面を表示する
   */
  @Override
  protected void setSelectionChangeHandler() {
    this.selectionChangeHandler = new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        PlusActivity selected = selectionModel.getSelectedObject();
        if (selected != null) {
          Window.open(selected.getUrl(), "PlusActivity", WindowOption.ITEM_DETAIL.getValue());
        }
      }
    };
  }

}
