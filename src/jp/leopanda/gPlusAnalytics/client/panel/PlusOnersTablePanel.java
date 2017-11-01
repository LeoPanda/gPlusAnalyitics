package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.panel.parts.ButtonColumn;
import jp.leopanda.gPlusAnalytics.client.panel.parts.ImageColumn;
import jp.leopanda.gPlusAnalytics.client.panel.parts.PlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.parts.StringColumn;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * @author LeoPanda
 *
 */
public class PlusOnersTablePanel extends PlusItemTable<PlusPeople> {

  ImageColumn<PlusPeople> imageColumn; // 写真
  StringColumn<PlusPeople> nameColumn; // ユーザー名
  ButtonColumn<PlusPeople> filterButton; // +1erフィルターボタン

  /**
   * コンストラクタ
   * 
   * @param items 一覧に表示するアイテムデータ
   */
  public PlusOnersTablePanel(List<PlusPeople> items) {
    super(items);
    setSrotHandler(handler -> setSortRequirement(handler));
  }

  /*
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    imageColumn = new ImageColumn<PlusPeople>("50", "50", item -> item.getImageUrl());
    nameColumn = new StringColumn<PlusPeople>(item -> item.getDisplayName());
    filterButton = new ButtonColumn<PlusPeople>(item -> String.valueOf(item.getNumOfPlusOne()));
    filterButton.addClickEvent(
        (index, item) -> setButtonClickEvent(item, FilterType.ACTIVITIES_BY_PLUSONER));

    // カラム表示リストに登録
    addColumnSet("", imageColumn, 50, null);
    addColumnSet("名前", nameColumn, 200, null);
    addColumnSet("+1", filterButton, 50, HasHorizontalAlignment.ALIGN_RIGHT);
  }

  /**
   * カラムのソート条件をセットする
   */
  private void setSortRequirement(ListHandler<PlusPeople> sortHandler) {
    filterButton.setSortable(true);
    sortHandler.setComparator(filterButton,
        Comparator.comparing(PlusPeople::getNumOfPlusOne).reversed());
  }
}
