package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Date;
import java.util.Optional;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.PhotoCalcUtil;
import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.DetailPopRequestListener;

/**
 * 表示用写真オブジェクト
 * 
 * 
 * @author LeoPanda
 *
 */
class PhotoImage extends FocusPanel {
  Date published;
  int gridHeight;
  double defaultGridWidth;
  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  DetailPopRequestListener detailPopRequestListener;
  SquareDimensions photoDimensions = null;

  /**
   * コンストラクタ
   * 
   * @param activity
   */
  PhotoImage(final PlusActivity activity, int gridHeight, DetailPopRequestListener listener) {
    this.published = activity.getPublished();
    this.gridHeight = gridHeight;
    this.defaultGridWidth = getDefaultWidth(activity, gridHeight);
    activity.getAttachmentImageUrls().ifPresent(urls -> {
      this.add(new ImageObject(activity, gridHeight));
      this.detailPopRequestListener = listener;
      this.addStyleName(MyStyle.PHOTO_CELL.getStyle());
    });
  }

  /**
   * デフォルトの写真オブジェクト表示幅を設定する
   * 
   * @param activity
   * @return
   */
  private double getDefaultWidth(PlusActivity activity, int gridHeight) {
    return calcUtil.getTemporarryImageWidth(activity, gridHeight);
  }

  /**
   * 写真オブジェクトの表示幅を提供する
   * 
   * 写真オブジェクトの外寸はImageObjectのonLoad()イベントで再計算されるので
   * イベント発生前はデフォルト値を提供する
   * 
   * @return
   */
  public double getPhotoWidth() {
    return Optional.ofNullable(photoDimensions).isPresent() ? getRealImageWidth()
        : this.defaultGridWidth;
  }

  /**
   * イメージの実測値から表示幅を設定する
   * @return
   */
  private double getRealImageWidth() {
    return (double) gridHeight * photoDimensions.getWidth() / photoDimensions.getHeight();
  }

  /**
   * 写真オブジェクトの投稿日を取得する
   * 
   * @return
   */
  public Date getPublished() {
    return this.published;
  }

  /**
   * イメージオブジェクト本体
   * 
   * @author LeoPanda
   *
   */
  private class ImageObject extends Image {

    ImageObject(PlusActivity activity, int height) {
      super();
      activity.getAttachmentImageUrls().ifPresent(urls -> this.setUrl(urls.get(0)));
      this.setWidth(
          Statics.getLengthWithUnit((int) calcUtil.getTemporarryImageWidth(activity, height)));
      this.addStyleName(MyStyle.GRID_IMAGE.getStyle());
      this.addClickHandler(setClickHandler(activity));
    }

    /**
     * ブラウザにロード後、イメージの表示幅を再設定する
     */
    @Override
    public void onLoad() {
      if (photoDimensions == null) {
        photoDimensions = new SquareDimensions(this.getWidth(), this.getHeight());
      } else {
        this.setWidth(Statics.getLengthWithUnit((int) getRealImageWidth()));
      }
      super.onLoad();
    }

    /**
     * クリックハンドラの設定
     * 
     * @param activity
     */
    private ClickHandler setClickHandler(final PlusActivity activity) {
      return event -> {
        detailPopRequestListener.request(activity, photoDimensions,
            Window.getScrollLeft() + event.getClientX(),
            Window.getScrollTop() + event.getClientY());
      };
    }

  }

}