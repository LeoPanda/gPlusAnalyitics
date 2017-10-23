package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadHandler;
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
  ImageObject image;
  double defaultGridWidth;
  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  DetailPopRequestListener detailPopRequestListener;
  SquareDimensions photoDimensions = null;

  Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * コンストラクタ
   * 
   * @param activity
   */
  PhotoImage(final PlusActivity activity, int gridHeight, DetailPopRequestListener listener) {
    this.published = activity.getPublished();
    this.gridHeight = gridHeight;
    defaultGridWidth = getDefaultWidth(activity, gridHeight);
    image = new ImageObject(activity, gridHeight);
    activity.getFirstAttachmentImageUrl().ifPresent(url -> {
      image = new ImageObject(activity, gridHeight);
      add(image);
      detailPopRequestListener = listener;
      addStyleName(MyStyle.PHOTO_CELL.getStyle());
    });
  }

  /**
   * 写真オブジェクトのonLoadイベントにアクションを追加する
   * 写真オブジェクトが存在しない場合はfalseを返す
   * 
   * @param action
   * @return
   */
  public boolean actionOnLoadImage(Consumer<Boolean> action) {
    Optional<ImageObject> imageValue = Optional.ofNullable(image);
    if (imageValue.isPresent()) return imageValue.get().setOnloadAction(action);
    return false;
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
   * @return
   */
  public double getPhotoWidth() {
    return Optional.ofNullable(photoDimensions).isPresent() ? getRealImageWidth()
        : this.defaultGridWidth;
  }

  /**
   * イメージの実測値から表示幅を計算する
   * 
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
    Consumer<Boolean> onLoadAction;

    /**
     * コンストラクタ
     * 
     * @param activity
     * @param height
     */
    ImageObject(PlusActivity activity, int height) {
      super();
      activity.getFirstAttachmentImageUrl().ifPresent(url -> this.setUrl(url));
      this.setWidth(
          Statics.getLengthWithUnit((int) calcUtil.getTemporarryImageWidth(activity, height)));
      this.addStyleName(MyStyle.GRID_IMAGE.getStyle());
      this.addClickHandler(getClickHandler(activity));
      this.addLoadHandler(getLoadHandler());
    }

    /**
     * イメージを表示後、実測外寸を取得しオンロードアクションを実行する
     * 
     * @return
     */
    private LoadHandler getLoadHandler() {
      return (event) -> {
        if (photoDimensions == null) {
          photoDimensions = new SquareDimensions(getWidth(), getHeight());
        this.setWidth(Statics.getLengthWithUnit((int) getRealImageWidth()));
          Optional.ofNullable(onLoadAction).ifPresent(action -> action.accept(true));
        }
      };
    }

    /**
     * イメージのオンロードアクションを設定する
     * 
     * @param onloadAction
     * @return
     */
    private boolean setOnloadAction(Consumer<Boolean> onloadAction) {
      if (this.getUrl().trim().length() > 0) {
        this.onLoadAction = onloadAction;
        return true;
      }
      return false;
    }

    /**
     * クリックハンドラの設定
     * 
     * @param activity
     */
    private ClickHandler getClickHandler(final PlusActivity activity) {
      return event -> {
        detailPopRequestListener.request(activity, photoDimensions,
            Window.getScrollLeft() + event.getClientX(),
            Window.getScrollTop() + event.getClientY());
      };
    }

  }

}