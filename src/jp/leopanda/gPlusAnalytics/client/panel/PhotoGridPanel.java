package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.panel.abstracts.PageControlLine;
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
  final String panelWidth = "1150px";

  PageControlPanel pageControlPanel = new PageControlPanel(panelWidth);
  VerticalPanel gridLinePanel = new VerticalPanel();
  Label pageLabel = new Label();

  FilterableSourceItems items;
  List<PhotoGrid> gridLines;// グッリドライン
  int activityListSize;// アクテビティ全件のサイズ
  int storedDoneIndex; // グリッドラインに取り込み完了したアクテビティのインデックス
  int currentPage; // カレントのページ番号

  /**
   * コンストラクタ
   * 
   * @param activities
   */
  PhotoGridPanel(FilterableSourceItems items) {
    this.items = items;
    gridLinePanel.setWidth(panelWidth);
    this.add(pageControlPanel);
    this.add(gridLinePanel);
    pageControlPanel.getFreeSpace().add(pageLabel);

    loadGridLines(items.getActivities());
  }

  /**
   * グリッドの再表示
   * 
   * @param activities
   */
  public void reload() {
    gridLines.clear();
    gridLinePanel.clear();
    loadGridLines(items.getActivities());
  }

  /**
   * グリッドの表示
   * 
   * @param activities
   */
  private void loadGridLines(List<PlusActivity> activities) {
    activityListSize = activities.size();
    storedDoneIndex = 0;
    currentPage = 0;
    gridLines = getAllGrid(activities);
    displayRow(currentPage);
  }

  /**
   * 指定ページのグリッドを表示する
   * 
   * @param page
   */
  private void displayRow(int page) {
    gridLinePanel.clear();
    setPageLabel(page);
    Divider divider = new Divider();
    for (int row = 0; row < maxPageRows; row++) {
      int currentIndex = page * maxPageRows + row;
      if (gridLines.size() > currentIndex) {
        PhotoGrid currentGrid = gridLines.get(currentIndex);
        if (divider.checkLabel(currentGrid.getGroupBy())) {
          gridLinePanel.add(divider.getLabel(panelWidth));
        }
        gridLinePanel.add(currentGrid.getGrid());
      }
    }
  }

  /**
   * ページ番号ラベルのセット
   * 
   * @param page
   */
  private void setPageLabel(int page) {
    String pageLabel = String.valueOf(page + 1) + "/" + String.valueOf(getMaxPage() + 1);
    this.pageLabel.setText(pageLabel);
  }

  /**
   * すべてのアクテビティをグリッドラインに取り込む
   * 
   * @param activities
   * @return
   */
  private List<PhotoGrid> getAllGrid(List<PlusActivity> activities) {
    List<PhotoGrid> gridLines = new ArrayList<PhotoGrid>();
    while (storedDoneIndex < activityListSize) {
      gridLines.add(getGrid(activities));
    }
    return gridLines;
  }

  /**
   * 写真グリッドを生成する
   * 
   * @return
   */
  private PhotoGrid getGrid(List<PlusActivity> activities) {
    PhotoGrid grid = new PhotoGrid();
    int col;
    for (col = 0; col < maxCols; col++) {
      if (activityListSize <= col + storedDoneIndex) {
        storedDoneIndex = activityListSize;
        return grid;
      }
      PlusActivity activity = activities.get(col + storedDoneIndex);
      String groupBy = Formatter.getYYMString(activity.getPublished());
      if (!grid.addGrid(activity, groupBy)) {
        storedDoneIndex += col;
        return grid;
      }
    }
    storedDoneIndex += col;
    return grid;
  }

  /**
   * 最大ページ番号を取得する
   * 
   * @return
   */
  private int getMaxPage() {
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
      displayRow(currentPage);
    }

    @Override
    public void onLastPageButtonClick(ClickEvent event) {
      // 最後ページボタンが押された
      currentPage = getMaxPage();
      displayRow(currentPage);
    }

    @Override
    public void onPrevPageButtonClick(ClickEvent event) {
      // 前ボタンが押された
      currentPage = currentPage < 1 ? 0 : currentPage - 1;
      displayRow(currentPage);
    }

    @Override
    public void onNextPageButtonClick(ClickEvent event) {
      // 次ボタンが押された
      int maxPage = getMaxPage();
      currentPage = (currentPage + 1 > maxPage) ? maxPage : currentPage + 1;
      displayRow(currentPage);
    }

  }
}
