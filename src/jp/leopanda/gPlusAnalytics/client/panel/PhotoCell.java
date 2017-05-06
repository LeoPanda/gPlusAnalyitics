package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Date;

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
class PhotoCell extends FocusPanel {
  PhotoImage photoImage;
  PlusActivity activity;
  int gridHeight;
  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  DetailPopRequestListener detailPopRequestListener;
  SquareDimensions photoDimensions = null;

  /**
   * コンストラクタ
   * 
   * @param activity
   */
  PhotoCell(final PlusActivity activity, int gridHeight, DetailPopRequestListener listener) {
    this.activity = activity;
    if (activity.getAttachmentImageUrls() != null) {
      this.add(new PhotoImage(activity, gridHeight));
      this.detailPopRequestListener = listener;
      this.gridHeight = gridHeight;
      this.addStyleName(MyStyle.PHOTO_CELL.getStyle());
    }
  }

  /**
   * 写真オブジェクトの表示幅を計算する
   * 
   * @return
   */
  public double getPhotoWidth() {
    if (photoDimensions == null) {
      return calcUtil.getTemporarryPhotoWidth(activity, gridHeight);
    }
    return (double) gridHeight * photoDimensions.getWidth() / photoDimensions.getHeight();
  }

  /**
   * 写真オブジェクトの投稿日を取得する
   * 
   * @return
   */
  public Date getPublished() {
    return activity.getPublished();
  }

  /**
   * 写真セル
   * 
   * @author LeoPanda
   *
   */
  private class PhotoImage extends Image {

    PhotoImage(PlusActivity activity, int height) {
      super();
      this.setUrl(activity.getAttachmentImageUrls().get(0));
      this.setWidth(
          Statics.getLengthWithUnit((int) calcUtil.getTemporarryPhotoWidth(activity, height)));
      this.addStyleName(MyStyle.GRID_IMAGE.getStyle());
      this.addClickHandler(setClickHandler(activity));
    }

    /*
     * ブラウザにロード後、イメージの縦横サイズを検出する
     */
    @Override
    public void onLoad() {
      if (photoDimensions == null) {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
          photoDimensions = new SquareDimensions(this.getWidth(), this.getHeight());
          this.setWidth(Statics.getLengthWithUnit((int) getPhotoWidth()));
          this.setHeight(Statics.getLengthWithUnit(gridHeight));
        }
      }
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
          PhotoCell.this.detailPopRequestListener.request(activity,
              new SquareDimensions(PhotoImage.this.getWidth(),
                  PhotoImage.this.getHeight()),
              new SquareDimensions(event.getClientX(), event.getClientY()));
        }
      };
    }

  }

}