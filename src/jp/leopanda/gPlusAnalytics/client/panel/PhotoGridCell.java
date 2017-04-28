package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.PhotoCalcUtil;
import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.DetailPopRequestListener;

/**
 * 写真表示グッリッドセル
 * 
 * @author LeoPanda
 *
 */
class PhotoGridCell extends FocusPanel {
  PhotoCell photoCell;
  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  DetailPopRequestListener detailPopRequestListener;
  SquareDimensions photoDimensions = null;

  /**
   * コンストラクタ
   * 
   * @param activity
   */
  PhotoGridCell(final PlusActivity activity, int height, DetailPopRequestListener listener) {
    if (activity.getAttachmentImageUrls() != null) {
      this.add(new PhotoCell(activity, height));
      this.detailPopRequestListener = listener;
    }
  }

  public SquareDimensions getPhotoDimensinos(){
    return this.photoDimensions;
  }
  
  /**
   * 写真セル
   * 
   * @author LeoPanda
   *
   */
  private class PhotoCell extends Image {

    PhotoCell(PlusActivity activity, int height) {
      super();
      this.setUrl(activity.getAttachmentImageUrls().get(0));
      this.setWidth(
          Statics.getLengthWithUnit((int) calcUtil.getPhotoWidthOnGrid(activity, height)));
      this.addStyleName(MyStyle.GRID_IMAGE.getStyle());
      this.addClickHandler(setClickHandler(activity));
    }

    @Override
    public void onLoad() {
      PhotoGridCell.this.photoDimensions = new SquareDimensions(this.getWidth(), this.getHeight());
      super.onLoad();
    }

    /**
     * クリックハンドラの設定
     * 
     * @param activity
     */
    private ClickHandler setClickHandler(final PlusActivity activity) {
      return new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          PhotoGridCell.this.detailPopRequestListener.request(activity,
              new SquareDimensions(PhotoCell.this.getWidth(), PhotoCell.this.getHeight()));
        }
      };
    }

  }

}