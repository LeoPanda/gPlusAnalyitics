package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.HtmlBuilder;
import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.PlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SafeHtmlColumn;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * @author LeoPanda
 *
 */
public class PlusOnersTable extends PlusItemTable<PlusPeople> {

  /**
   * コンストラクタ
   * 
   * @param items 一覧に表示するアイテムデータ
   */
  public PlusOnersTable(List<PlusPeople> items) {
    super(items);

  }

  SafeHtmlColumn<PlusPeople> imageColumn;
  TextColumn<PlusPeople> nameColumn;
  ButtonColumn<PlusPeople> filterButton;

  @Override
  protected void setColumns() {
    // 写真
    imageColumn = new SafeHtmlColumn<PlusPeople>() {
      @Override
      public SafeHtml getValue(PlusPeople item) {
        HtmlBuilder builder = new HtmlBuilder();
        if (item.getImageUrl() != null) {
          builder.appendPeopleTumbnailImg(item.getImageUrl());
        }
        return builder.getSafeHtml();
      }
    };
    // ユーザー名
    nameColumn = new TextColumn<PlusPeople>() {
      @Override
      public String getValue(PlusPeople item) {
        return item.getDisplayName();
      }
    };
    // +1数 フィルターボタン
    filterButton = new ButtonColumn<PlusPeople>() {
      @Override
      public void addClickEvent(int index, PlusPeople item) {
        disableOnselectEventTemporally();
        itemEventListener.onEvent(item);
      }

      @Override
      public String getValue(PlusPeople item) {
        return String.valueOf(item.getNumOfPlusOne());
      }
    };
    filterButton.setCellStyleNames(CssStyle.BUTTON_FILTER.getName());

    // カラム表示リストに登録
    this.columSets.add(newColumnSet("", imageColumn, 50, null));
    this.columSets.add(newColumnSet("名前", nameColumn, 200, null));
    this.columSets.add(newColumnSet("+1", filterButton, 50, HasHorizontalAlignment.ALIGN_RIGHT));
  }

  /**
   * カラムのソート条件をセットする
   */
  @Override
  protected void setSortHandler() {
    filterButton.setSortable(true);
    sortHandler.setComparator(filterButton, new Comparator<PlusPeople>() {
      @Override
      public int compare(PlusPeople o1, PlusPeople o2) {
        return o1.getNumOfPlusOne() - o2.getNumOfPlusOne();
      }
    });
  }
}
