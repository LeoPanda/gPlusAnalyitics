package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.DateDivider;

/**
 * 写真一覧パネルの１ページ分の表示を構成する補助クラス
 * 
 * @author LeoPanda
 *
 */
public class PhotoPanelPageMaker {

  PhotoCellList allPhotoCells;

  private VerticalPanel pagePanel;

  private int currentIndex;
  private int currentRow;
  private DateDivider publishDateDivider;
  private PhotoPanelDisplayCell currentCell = null;

  private final int pageRowSize = 6;
  private final int pageWidth = 1100;
  private final int lineHeight = 120;
  private final int labelCellWidth = 40;
  private final int cellSpacing = 2;

  /**
   * ソースデータの読み込み
   * 
   * @param allPhotoCells
   */
  public void setSourceData(PhotoCellList allPhotoCells) {
    this.allPhotoCells = allPhotoCells;
  }

  /**
   * リセットする
   */
  public void clear() {
    currentCell = null;
  }

  /**
   * 次ページへ引き継ぐ変数を返す
   * 
   * @return
   */
  public PhotoPanelPageKeeper getStocker() {
    return new PhotoPanelPageKeeper(this.currentIndex, this.publishDateDivider.getCurrentDate(),
        this.currentCell);
  }

  /**
   * 表示行の高さを返す
   * 
   * @return
   */
  public int getLineHeight() {
    return this.lineHeight;
  }

  /**
   * ページの幅を返す
   * 
   * @return
   */
  public int getPageWidth() {
    return this.pageWidth;
  }

  /**
   * ページの最大行数を返す
   * 
   * @return
   */
  public int getPageRowSize() {
    return this.pageRowSize;
  }

  /**
   * 1ページ分の写真リストを生成する
   * 
   * @return
   */
  public VerticalPanel getPagePanel(PhotoPanelPageKeeper stocker) {
    setInitialValues(stocker);
    pagePanel = new VerticalPanel();
    while (currentRow < pageRowSize) {
      pagePanel.add(getPhotoLine());
      currentRow++;
    }
    return pagePanel;
  }

  /**
   * 変数初期化
   * 
   * @param currentIndex
   * @param publishDateDivider
   */
  private void setInitialValues(PhotoPanelPageKeeper stocker) {
    currentRow = 0;
    this.currentIndex = stocker.getCurrentIndex();
    this.publishDateDivider = new DateDivider(stocker.getCurrentPublishDate());
    this.currentCell = stocker.getCurrentCell();
  }

  /**
   * １行分の写真リストを生成する
   * 
   * @return
   */
  private HorizontalPanel getPhotoLine() {
    HorizontalPanel photoLine = new HorizontalPanel();
    photoLine.setWidth(Statics.getLengthWithUnit(pageWidth));
    photoLine.setSpacing(cellSpacing);
    int lineWidth = 0;
    if (currentCell != null) {
      photoLine.add(currentCell.getCell());
      lineWidth += currentCell.getWidth() + cellSpacing;
    }
    for (int i = currentIndex; i < allPhotoCells.size(); i++) {
      PhotoCell photoCell = allPhotoCells.get(i);
      switch (publishDateDivider.getBreakLevel(photoCell.getPublished())) {
      case YEAR_BREAK:
        if (photoLine.getWidgetCount() > 0) {
          pagePanel.add(photoLine);
          currentRow++;
          photoLine = new HorizontalPanel();
          lineWidth = 0;
        }
        currentCell = getYearBreakedCell(photoLine, photoCell);
        break;
      case MONTH_BREAK:
        currentCell = getMonthBreakedCell(photoCell);
        break;
      case NONE:
        currentCell = new PhotoPanelDisplayCell(photoCell, (int) photoCell.getPhotoWidth());
        break;
      default:
        break;
      }
      currentIndex++;
      lineWidth += currentCell.getWidth() + cellSpacing;
      if (lineWidth >= pageWidth) {
        break;
      } else {
        photoLine.add(currentCell.getCell());
        currentCell = null;
      }
    }
    return photoLine;
  }

  /**
   * 年レベルブレーク時の表示セルを提供する
   * 
   * @param photoLinePanel
   * @param photoCell
   * @return
   */
  private PhotoPanelDisplayCell getYearBreakedCell(HorizontalPanel photoLinePanel,
      PhotoCell photoCell) {
    pagePanel.add(getYearDivideLabel());
    return getMonthBreakedCell(photoCell);
  }

  /**
   * 月レベルブレーク時の表示セルを提供する
   * 
   * @param photoCell
   * @return
   */
  private PhotoPanelDisplayCell getMonthBreakedCell(PhotoCell photoCell) {
    HorizontalPanel labeledCell = new HorizontalPanel();
    labeledCell.add(getMonthDivideLabel());
    labeledCell.add(photoCell);
    return new PhotoPanelDisplayCell(labeledCell,
        (int) (labelCellWidth + photoCell.getPhotoWidth()));
  }

  /**
   * 投稿年ブレークラベルを取得する
   * 
   * @return
   */
  private Label getYearDivideLabel() {
    Label breakLabel = publishDateDivider.getYearLabel();
    breakLabel.setWidth(Statics.getLengthWithUnit(pageWidth));
    breakLabel.setStyleName(MyStyle.YEAR_LABEL.getStyle());
    return breakLabel;
  }

  /**
   * 投稿月ブレークラベルを取得する
   * 
   * @return
   */
  private Label getMonthDivideLabel() {
    Label breakLabel = publishDateDivider.getMonthLabel();
    breakLabel.setWidth(Statics.getLengthWithUnit(labelCellWidth));
    breakLabel.setHeight(Statics.getLengthWithUnit(lineHeight));
    breakLabel.setStyleName(MyStyle.MONTH_LABEL.getStyle());
    return breakLabel;
  }

}
