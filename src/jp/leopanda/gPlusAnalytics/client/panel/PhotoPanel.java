package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.DetailPopRequestListener;

/**
 * 写真一覧パネル
 *  PhotoPanel -- PhotoCellList -- PhotoCell
 * 
 * @author LeoPanda
 *
 */
public class PhotoPanel extends VerticalPanel {
  FilterableSourceItems items;
  PhotoCellList allPhotoCells;
  PhotoDetailPop photoDetailPop;
  Button nextPageButton = new Button("more..");
  Label pageIndexLabel = new Label();
  VerticalPanel listPanel = new VerticalPanel();

  PhotoPanelPageMaker pageMaker = new PhotoPanelPageMaker();
  PhotoPanelPageKeeper pageKeeper = new PhotoPanelPageKeeper();

  /**
   * コンストラクタ
   * 
   * @param items
   */
  public PhotoPanel(FilterableSourceItems items) {
    this.items = items;
    this.setHorizontalAlignment(ALIGN_RIGHT);
    this.add(getIndexLine());
    this.setHorizontalAlignment(ALIGN_LEFT);
    this.add(listPanel);
    this.setHorizontalAlignment(ALIGN_RIGHT);
    this.add(nextPageButton);
    nextPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        addOnePage();
      }
    });
    addFirstPage(items);
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
  private void addFirstPage(FilterableSourceItems items) {
    allPhotoCells = loadAllPhotoCells(items.getActivities());
    pageMaker.setSourceData(allPhotoCells);
    addOnePage();
  }

  /**
   * １ページ分の写真リストをページに追加する
   */
  private void addOnePage() {
    // 写真サイズ確定のための仮読み込み
    listPanel.add(pageMaker.getPagePanel(pageKeeper));
    listPanel.remove(listPanel.getWidgetCount() - 1);
    // 本読み込み
    listPanel.add(pageMaker.getPagePanel(pageKeeper));
    // 読込結果をフィードバック
    this.pageKeeper = pageMaker.getStocker();
    if (pageKeeper.getCurrentIndex() < allPhotoCells.size()) {
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
        .setText(String.valueOf(pageKeeper.getCurrentIndex()) + "/"
            + String.valueOf(allPhotoCells.size()) + "件");
  }

  /**
   * 画面の再表示
   */
  public void reload() {
    pageKeeper.clear();
    pageMaker.clear();
    allPhotoCells.clear();
    listPanel.clear();
    addFirstPage(items);
  }

  /**
   * 全写真リストの読み込み
   * 
   * @param activities
   * @return
   */
  private PhotoCellList loadAllPhotoCells(List<PlusActivity> activities) {
    PhotoCellList photoCellList = new PhotoCellList();
    for (PlusActivity activity : activities) {
      photoCellList.addCell(activity, pageMaker.getLineHeight(), new DetailPopRequestListener() {
        @Override
        public void request(PlusActivity activity, SquareDimensions photoDimensions,int clickX,int clickY ) {
          popActivityDetail(activity, photoDimensions, clickX,clickY);
        }
      });
    }
    return photoCellList;
  }

  /**
   * アクテビティ詳細ウィンドウの表示
   * 
   * @param activity
   */
  private void popActivityDetail(PlusActivity activity, SquareDimensions photoDimensions,int clickX,int clickY) {
    if (photoDetailPop == null) {
      photoDetailPop = new PhotoDetailPop(
          new SquareDimensions(pageMaker.getPageWidth(),
              pageMaker.getLineHeight() * pageMaker.getPageRowSize()));
    }
    photoDetailPop.show(activity, photoDimensions, clickX,clickY);
  }

}
