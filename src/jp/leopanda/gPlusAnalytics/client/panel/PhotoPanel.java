package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 写真一覧パネル
 * PhotoPanel -- PhotoCellList -- PhotoCell
 * 
 * @author LeoPanda
 *
 */
public class PhotoPanel extends VerticalPanel {
  FilterableSourceItems sourceItems;
  PhotoImageList sourcePhotoImages;
  PhotoDetailPopPanel photoDetailPopPanel;
  Button nextPageButton = new Button("more..");
  Label pageIndexLabel = new Label();
  VerticalPanel photoListPanel = new VerticalPanel();

  PhotoPanelPageGenerator pageGenerator = new PhotoPanelPageGenerator();
  PageTag pageTag = new PageTag();

  /**
   * コンストラクタ
   * 
   * @param sourceItems
   */
  public PhotoPanel(FilterableSourceItems sourceItems) {
    this.sourceItems = sourceItems;
    sourcePhotoImages = getPhotoImageList(sourceItems.getActivities());
    setPanel();
    addFirstPage();
  }

  /**
   * パネルの初期設定
   */
  private void setPanel() {
    nextPageButton.addClickHandler(event -> addOnePage());
    addWidthAlign(getIndexLine(), ALIGN_RIGHT);
    addWidthAlign(photoListPanel, ALIGN_LEFT);
    addWidthAlign(nextPageButton, ALIGN_RIGHT);
  }

  /**
   * 割付を指定してパネルにウィジェットを追加する
   * 
   * @param widget
   * @param align
   */
  private void addWidthAlign(Widget widget, HorizontalAlignmentConstant align) {
    this.setHorizontalAlignment(align);
    this.add(widget);
  }

  /**
   * ページのインデックス表示行を作成する
   * 
   * @return
   */
  private HorizontalPanel getIndexLine() {
    HorizontalPanel indexLine = new HorizontalPanel();
    indexLine.add(pageIndexLabel);
    return indexLine;
  }

  /**
   * 最初の１ページを表示する
   * 
   * @param items
   */
  private void addFirstPage() {
    pageGenerator.setSourceData(sourcePhotoImages);
    addOnePage();
  }

  /**
   * １ページ分の写真リストをページに追加する
   */
  private void addOnePage() {
    // 写真サイズ確定のための仮読み込み
    photoListPanel.add(pageGenerator.getPagePanel(pageTag));
    photoListPanel.remove(photoListPanel.getWidgetCount() - 1);
    // 本読み込み
    photoListPanel.add(pageGenerator.getPagePanel(pageTag));
    // 読込結果をフィードバック
    pageTag = pageGenerator.getPageTag();
    if (pageTag.getCurrentIndex() < sourcePhotoImages.size()) {
      nextPageButton.setVisible(true);
    } else {
      nextPageButton.setVisible(false);
    }
    setIndexLabel();
  }

  /**
   * インデックスラベルをセットする
   */
  private void setIndexLabel() {
    pageIndexLabel
        .setText(String.valueOf(pageTag.getCurrentIndex()) + "/"
            + String.valueOf(sourcePhotoImages.size()) + "件");
  }

  /**
   * 画面の再表示
   */
  public void reload() {
    pageTag.clear();
    pageGenerator.clear();
    photoListPanel.clear();
    sourcePhotoImages.clear();
    sourcePhotoImages = getPhotoImageList(sourceItems.getActivities());
    addFirstPage();
  }

  /**
   * 写真リストの作成
   * 
   * @param activities
   * @return
   */
  private PhotoImageList getPhotoImageList(List<PlusActivity> activities) {
    PhotoImageList photoImageList = new PhotoImageList();
    activities.stream().sorted(Comparator.comparing(PlusActivity::getPublished))
        .forEach(activity -> photoImageList.addImage(activity, pageGenerator.getLabelHeight(),
            this::popActivityDetail));
    return photoImageList;
  }

  /**
   * アクテビティ詳細ウィンドウの表示
   * 
   * @param activity
   */
  private void popActivityDetail(PlusActivity activity, SquareDimensions photoDimensions,
      int clickX, int clickY) {
    photoDetailPopPanel = Optional.ofNullable(photoDetailPopPanel)
        .orElse(new PhotoDetailPopPanel(
            new SquareDimensions(pageGenerator.getPageWidth(),
                pageGenerator.getLabelHeight() * pageGenerator.getPageRowSize())));
    photoDetailPopPanel.show(activity, photoDimensions, clickX, clickY);
  }

}
