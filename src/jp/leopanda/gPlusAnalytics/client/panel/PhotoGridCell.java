package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.PhotoCalcUtil;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 写真表示グッリッドセル
 * 
 * @author LeoPanda
 *
 */
class PhotoGridCell extends FocusPanel {
  ActivityDetailPop activityDetailPop = null;
  PhotoCell photoCell;
  PhotoCalcUtil calcUtil = new PhotoCalcUtil();

  /**
   * コンストラクタ
   * 
   * @param activity
   */
  PhotoGridCell(final PlusActivity activity, int height) {
    if (activity.getAttachmentImageUrls() != null) {
      this.add(new PhotoCell(activity, height));
    }
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
      this.setWidth(Statics.getLengthWithUnit((int) calcUtil.getPhotoWidthOnGrid(activity, height)));
      this.addStyleName(MyStyle.GRID_IMAGE.getStyle());
      this.addClickHandler(setClickHandler(activity));
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
          if (activityDetailPop == null) {
            activityDetailPop = new ActivityDetailPop(activity, 
                event.getClientX(),event.getClientY());
          } else {
            activityDetailPop.reShow(event.getClientX(),event.getClientY());
          }
        }
      };
    }

  }

}