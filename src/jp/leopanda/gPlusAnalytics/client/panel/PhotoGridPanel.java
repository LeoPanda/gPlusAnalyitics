package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.Unit;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.PageControlLine;
import jp.leopanda.gPlusAnalytics.client.util.CalcUtil;
import jp.leopanda.gPlusAnalytics.client.util.Divider;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 投稿写真をグリッド表示するパネル
 * 
 * @author LeoPanda
 *
 */
public class PhotoGridPanel extends VerticalPanel {
  final int maxPageRows = 6;// １ページに表示するグリッドラインの行数
  final int maxCols = 6;// １行に表示するグリッドのカラム数
  final int panelWidth = 1150;
  final int gridHeight = 120;

  PageControlPanel pageControlPanel = new PageControlPanel(
      Unit.getStringWithLength((int) panelWidth));
  VerticalPanel gridLinePanel = new VerticalPanel();
  Label pageLabel = new Label();

  FilterableSourceItems items;
  List<PhotoGridLine> gridLines;// グッリドライン
  int currentPage; // カレントのページ番号

  /**
   * コンストラクタ
   * 
   * @param activities
   */
  PhotoGridPanel(FilterableSourceItems items) {
    this.items = items;
    gridLinePanel.setWidth(Unit.getStringWithLength((int) panelWidth));
    this.add(pageControlPanel);
    this.add(gridLinePanel);
    pageControlPanel.getFreeSpace().add(pageLabel);

    loadGridLines(items.getActivities());
  }

  /**
   * パネルの再表示
   * 
   * @param activities
   */
  public void reload() {
    gridLines.clear();
    gridLinePanel.clear();
    loadGridLines(items.getActivities());
  }

  /**
   * 全アクテビティをグリッドに読み込み、最初のページを表示する
   * 
   * @param activities
   */
  private void loadGridLines(List<PlusActivity> activities) {
    currentPage = 0;
    gridLines = getAllGridLines(activities);
    displayPage(currentPage);
  }

  /**
   * 指定ページのグリッドを表示する
   * 
   * @param page
   */
  private void displayPage(int page) {
    gridLinePanel.clear();
    setPageLabel(page);
    Divider divider = new Divider();
    for (int row = 0; row < maxPageRows; row++) {
      int currentIndex = row + page * maxPageRows;
      if (currentIndex < gridLines.size()) {
        PhotoGridLine currentGrid = gridLines.get(currentIndex);
        if (divider.checkLabel(currentGrid.getGroupBy())) {
          gridLinePanel.add(divider.getLabel(Unit.getStringWithLength((int) panelWidth)));
        }
        Grid grid = currentGrid.getGrid();
        gridLinePanel.add(grid);
      }
    }
  }

  /**
   * ページ番号ラベルのセット
   * 
   * @param page
   */
  private void setPageLabel(int page) {
    String pageLabel = String.valueOf(page + 1) + "/" + String.valueOf(getMaxPageNumber() + 1);
    this.pageLabel.setText(pageLabel);
  }

  /**
   * アクテビティ全件分の写真グリッドを生成しリストに取り込む
   * 
   * @param activities
   * @return
   */
  private List<PhotoGridLine> getAllGridLines(List<PlusActivity> activities) {
    List<PhotoGridLine> allGridLines = new ArrayList<PhotoGridLine>();
    PhotoGridLine gridLine = new PhotoGridLine(gridHeight);
    CalcUtil calcUtil = new CalcUtil();
    double gridWidth = 0;
    boolean divided;
    for (PlusActivity activity : activities) {
      gridWidth += calcUtil.getPhotoWidth(activity,gridHeight);
      if (gridWidth > panelWidth) {
        divided = true;
      } else {
        divided = !gridLine.addGridByDivider(activity,
            Formatter.getYYMString(activity.getPublished()));
      }
      if (divided) {
        allGridLines.add(gridLine);
        gridLine = new PhotoGridLine(gridHeight);
        gridWidth = calcUtil.getPhotoWidth(activity,gridHeight);
        gridLine.addGridByDivider(activity, Formatter.getYYMString(activity.getPublished()));
      }
    }
    if (gridLine.size() > 0) {
      allGridLines.add(gridLine);
    }
    return allGridLines;
  }


  /**
   * 最大ページ番号を取得する
   * 
   * @return
   */
  private int getMaxPageNumber() {
    int maxPage = gridLines.size() / maxPageRows;
    if (gridLines.size() % maxPageRows == 0) {
      maxPage = maxPage - 1 > 0 ? maxPage - 1 : 0;
    }
    return maxPage;
  }

  /**
   * ページコントロールパネル
   * 
   * @author LeoPanda
   *
   */
  private class PageControlPanel extends PageControlLine {

    /**
     * コンストラクタ
     * 
     * @param width
     */
    public PageControlPanel(String width) {
      super(width);
    }

    @Override
    public void onFirstPageButtonClick(ClickEvent event) {
      // 最前ページボタンが押された
      currentPage = 0;
      displayPage(currentPage);
    }

    @Override
    public void onLastPageButtonClick(ClickEvent event) {
      // 最後ページボタンが押された
      currentPage = getMaxPageNumber();
      displayPage(currentPage);
    }

    @Override
    public void onPrevPageButtonClick(ClickEvent event) {
      // 前ボタンが押された
      currentPage = currentPage < 1 ? 0 : currentPage - 1;
      displayPage(currentPage);
    }

    @Override
    public void onNextPageButtonClick(ClickEvent event) {
      // 次ボタンが押された
      int maxPage = getMaxPageNumber();
      currentPage = (currentPage + 1 > maxPage) ? maxPage : currentPage + 1;
      displayPage(currentPage);
    }

  }
}
