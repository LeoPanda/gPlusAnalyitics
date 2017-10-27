package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.DateSeparator;

/**
 * 写真一覧パネルの１ページ分の表示を構成する補助クラス
 * 
 * @author LeoPanda
 *
 */
public class PhotoPanelPageGenerator {

  PhotoImageList sourcePhotoImageList;
  Consumer<Boolean> actionAfterLoadPage;

  private VerticalPanel pagePanel;
  private HorizontalPanel photoRowPanel;

  private final int rowLimitSize = 7;
  private final int pageWidth = 1100;
  private final int labelHeight = 120;
  private final int labelCellWidth = 40;
  private final int marginWidth = 2;

  private int currentIndex;// 処理中の写真インデックス
  private int currentRow;// 処理中のページ内行
  private int accumlateRowWidth;// 改行を検知するための行幅累積値
  private Date currentPublishedDate;// 処理中写真の投稿日
  private PhotoCell currentCell = null;

  private DateSeparator dateSeparator;
  Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * ソースデータの読み込み
   * 
   * @param sourcePhotoImageList
   */
  public void setSourceData(PhotoImageList sourcePhotoImageList) {
    this.sourcePhotoImageList = sourcePhotoImageList;
  }

  /**
   * オンロードリスナーの設定
   * 
   * @param action
   */
  public void setActionAfterLoadPage(Consumer<Boolean> action) {
    this.actionAfterLoadPage = action;
  }

  /**
   * リセットする
   */
  public void clear() {
    currentCell = null;
  }

  /**
   * カレントのページタグを返す
   * 
   * @return
   */
  public PageTag getPageTag() {
    PageTag pageTag = new PageTag();
    pageTag.set(this.currentIndex, this.currentPublishedDate, this.currentCell);
    return pageTag;
  }

  /**
   * ラベル行の高さを返す
   * 
   * @return
   */
  public int getLabelHeight() {
    return this.labelHeight;
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
    return this.rowLimitSize;
  }

  /**
   * 写真一覧パネルを１ページ分生成する
   * 
   * @return
   */
  public VerticalPanel getPagePanel(PageTag pageTag) {
    pagePanel = new VerticalPanel();
    setInitialValues(pageTag);
    setNewPhotoRowPanel();
    for (int i = currentIndex; i < sourcePhotoImageList.size(); i++) {
      try {
        setPhotoRowPanel(sourcePhotoImageList.get(i));
      } catch (PageBreaked e) {
        setPhotoImageOnLoadAction(i);
        break;
      }
    }
    if (currentIndex == sourcePhotoImageList.size()) {
      setPhotoImageOnLoadAction(currentIndex);
    }
    pagePanel.add(photoRowPanel);
    return pagePanel;
  }

  /**
   * 最後尾写真イメージロード後に実行するアクションを設定する
   * 
   * @param i
   */
  private void setPhotoImageOnLoadAction(int i) {
    i--;// ページブレーク時より１つ前がページ最後尾
    if (i < 0) return;
    PhotoImage photoImage = sourcePhotoImageList.get(i);
    if (!photoImage.actionOnLoadImage(e -> actionAfterLoadPage.accept(e))) {
      setPhotoImageOnLoadAction(i - 1);
    }
  }

  /**
   * 行パネルをセットする
   * 
   * @param photoImage
   * @throws PageBreaked
   */
  private void setPhotoRowPanel(PhotoImage photoImage) throws PageBreaked {
    currentCell = getCurrentCell(photoImage);
    int cellWidth = currentCell.getWidth() + marginWidth;
    if ((accumlateRowWidth += cellWidth) >= pageWidth) {
      doNewRow();
      accumlateRowWidth = cellWidth;
    }
    photoRowPanel.add(currentCell.getCell());
    currentIndex++;
    currentPublishedDate = photoImage.getPublished();
  }

  /**
   * 変数初期化
   * 
   * @param currentIndex
   * @param dateSeparator
   */
  private void setInitialValues(PageTag pageTag) {
    currentRow = 0;
    currentIndex = pageTag.getCurrentIndex();
    dateSeparator = new DateSeparator(pageTag.getCurrentPublishDate());
    currentCell = pageTag.getCurrentCell();
  }

  /**
   * 新しい表示行を用意する
   */
  private void setNewPhotoRowPanel() {
    photoRowPanel = new HorizontalPanel();
    photoRowPanel.setWidth(Statics.getLengthWithUnit(pageWidth));
    photoRowPanel.setSpacing(marginWidth);
    accumlateRowWidth = 0;
  }

  /**
   * 改行する
   * ページの最大行に達した場合には例外を発生させる
   * 
   * @throws PageBreaked
   */
  private void doNewRow() throws PageBreaked {
    if (photoRowPanel.getWidgetCount() > 0) {
      pagePanel.add(photoRowPanel);
      setNewPhotoRowPanel();
      currentRow++;
    }
    if (currentRow >= rowLimitSize) throw new PageBreaked();
  }

  /**
   * 写真表示セルを生成する
   * 
   * @param photoImage
   * @throws PageBreaked
   */
  private PhotoCell getCurrentCell(PhotoImage photoImage) throws PageBreaked {
    PhotoCell photoCell = null;
    switch (dateSeparator.getBreakLevel(photoImage.getPublished())) {
      case YEAR_BREAK:
        photoCell = getYearBreakedCell(photoRowPanel, photoImage);
        break;
      case MONTH_BREAK:
        photoCell = getMonthBreakedCell(photoImage);
        break;
      case NONE:
        photoCell = new PhotoCell(photoImage, (int) photoImage.getPhotoWidth());
        break;
      default:
        break;
    }
    return photoCell;
  }

  /**
   * 年レベルブレーク時の表示セルを提供する
   * 
   * @param photoLinePanel
   * @param photoImage
   * @return
   * @throws PageBreaked
   */
  private PhotoCell getYearBreakedCell(HorizontalPanel photoLinePanel,
      PhotoImage photoImage) throws PageBreaked {
    currentRow++;
    doNewRow();
    pagePanel.add(getYearDivideLabel());
    return getMonthBreakedCell(photoImage);
  }

  /**
   * 月レベルブレーク時の表示セルを提供する
   * 
   * @param photoImage
   * @return
   */
  private PhotoCell getMonthBreakedCell(PhotoImage photoImage) {
    HorizontalPanel labeledCell = new HorizontalPanel();
    labeledCell.add(getMonthDivideLabel());
    labeledCell.add(photoImage);
    return new PhotoCell(labeledCell,
        (int) (labelCellWidth + photoImage.getPhotoWidth()));
  }

  /**
   * 投稿年ブレークラベルを取得する
   * 
   * @return
   */
  private Label getYearDivideLabel() {
    Label breakLabel = dateSeparator.getYearLabel();
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
    Label breakLabel = dateSeparator.getMonthLabel();
    breakLabel.setWidth(Statics.getLengthWithUnit(labelCellWidth));
    breakLabel.setHeight(Statics.getLengthWithUnit(labelHeight));
    breakLabel.setStyleName(MyStyle.MONTH_LABEL.getStyle());
    return breakLabel;
  }

  /**
   * 行数がページの制限値に達した場合の通知用例外
   * 
   * @author LeoPanda
   *
   */
  private class PageBreaked extends Exception {
    private static final long serialVersionUID = 1L;
  }

}
