package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.SelectionChangeEvent;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SafeHtmlColumn;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.SimpleCellTable;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.ItemEventListener;

/**
 * @author LeoPanda
 *
 */
public class PlusOnersTable extends SimpleCellTable<PlusPeople> {

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

  boolean excludeSelectionChanged = false; // ボタンクリック時は行選択アクションから除外する
  ItemEventListener<PlusPeople> filterEventListener;// フィルター実行指示のイベントリスナー

  public void addFilterEventListener(ItemEventListener<PlusPeople> listener) {
    filterEventListener = listener;
  }

  @Override
  protected void setColumns() {
    // 写真
    imageColumn = new SafeHtmlColumn<PlusPeople>() {
      @Override
      public SafeHtml getValue(PlusPeople item) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant("<img chartHeight=\"50\" chartWidth =\"50\" src=\"")
            .appendEscaped(item.getImageUrl()).appendHtmlConstant("\">");
        return builder.toSafeHtml();
      }
    };
    // ユーザー名
    nameColumn = new TextColumn<PlusPeople>() {
      @Override
      public String getValue(PlusPeople item) {
        return item.getDisplayName();
      }
    };
    // アクテビティフィルターボタン
    filterButton = new ButtonColumn<PlusPeople>() {
      @Override
      public void addClickEvent(int index, PlusPeople item) {
        filterEventListener.onEvent(item);
        excludeSelectionChanged = true;
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

  /**
   * 行がクリックされたらg+プロファイル画面を表示する
   */
  @Override
  protected void setSelectionChangeHandler() {
    this.selectionChangeHandler = new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        PlusPeople selected = selectionModel.getSelectedObject();
        if (selected != null) {
          if (excludeSelectionChanged) {
            excludeSelectionChanged = false;
          } else {
            Window.open(selected.getUrl(), "PlusPeople", WindowOption.ITEM_DETAIL.getValue());
          }
        }
      }
    };
  }

}
