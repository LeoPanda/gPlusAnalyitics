package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
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

  Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * コンストラクタ
   * 
   * @param sourceItems
   */
  public PhotoPanel(FilterableSourceItems sourceItems) {
    this.sourceItems = sourceItems;
    sourcePhotoImages = getPhotoImageList(sourceItems.getActivities());
    setPageGenerator();
    setPanel();
    addOnePage();
  }

  /**
   * 画面の再表示
   */
  public void reload() {
    pageTag.clear();
    photoListPanel.clear();
    sourcePhotoImages.clear();
    pageGenerator.clear();
    sourcePhotoImages = getPhotoImageList(sourceItems.getActivities());
    pageGenerator.setSourceData(sourcePhotoImages);
    writeIndexLabel(pageTag);
    addOnePage();
  }

  /**
   * パネルの初期設定
   */
  private void setPanel() {
    nextPageButton.addClickHandler(event -> addOnePage());
    nextPageButton.setStyleName(MyStyle.NEXTPAGE_BUTTON.getStyle());
    Window.addWindowScrollHandler(event -> onWindowScroll());
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
    writeIndexLabel(pageTag);
    return indexLine;
  }

  /**
   * ページジェネレータをセットアップする
   */
  private void setPageGenerator() {
    pageGenerator.setSourceData(sourcePhotoImages);
    pageGenerator.setActionAfterLoadPage(e -> reWritePage());
  }

  /**
   * ウィンドウスクロール時に次ページを表示する
   */
  private void onWindowScroll(){
    if(nextPageButton.isVisible()){
      addOnePage();
    }
  }
  
  /**
   * １ページ分の写真リストをパネルに追加する
   */
  private void addOnePage() {
    photoListPanel.add(pageGenerator.getPagePanel(pageTag));
    writeIndexLabel(pageGenerator.getPageTag());
    nextPageButton.setVisible(false);
  }

  /**
   * 写真イメージをリサイズするためにページを書き直す
   */
  private void reWritePage() {
    photoListPanel.remove(photoListPanel.getWidgetCount() - 1);
    photoListPanel.add(pageGenerator.getPagePanel(pageTag));
    pageTag = pageGenerator.getPageTag();
    writeIndexLabel(pageTag);
    nextPageButton.setVisible(pageTag.getCurrentIndex() < sourcePhotoImages.size());

  }

  /**
   * インデックスラベルを表示する
   */
  private void writeIndexLabel(PageTag currnetPageTag) {
    pageIndexLabel
        .setText(String.valueOf(currnetPageTag.getCurrentIndex()) + "/"
            + String.valueOf(sourcePhotoImages.size()) + "件");
  }

  /**
   * 写真リストの作成
   * 
   * @param activities
   * @return
   */
  private PhotoImageList getPhotoImageList(List<PlusActivity> activities) {
    PhotoImageList photoImageList = new PhotoImageList();
    activities.stream().sorted(Comparator.comparing(PlusActivity::getPublished).reversed())
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
