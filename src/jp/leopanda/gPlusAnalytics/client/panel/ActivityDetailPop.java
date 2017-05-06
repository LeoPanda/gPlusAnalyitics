package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.client.util.PhotoCalcUtil;
import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティ詳細ポップアップ画面
 * 
 * @author LeoPanda
 *
 */
public class ActivityDetailPop extends PopupPanel {
  Label published = new Label();
  Label accessDescription = new Label();
  Label title = new Label();
  Image image;

  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  SquareDimensions parentPanelDimensions;

  final double ajustY = 0.6;

  /**
   * コンストラクタ
   * 
   * @param activity
   * @param posX
   * @param posY
   */
  public ActivityDetailPop(SquareDimensions parentPanelDimensions) {
    super();
    this.parentPanelDimensions = parentPanelDimensions;
    published.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    accessDescription.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    this.addStyleName(MyStyle.DETAIL_POPWINDOW.getStyle());
    sinkEvents(Event.ONCLICK);
    sinkEvents(Event.ONMOUSEOUT);
    addDomHandler(setMouseOutHanlder(), MouseOutEvent.getType());
    setClickHandler();
  }

  /**
   * クリックハンドラの設定
   * 
   * @param activity
   */
  private void setClickHandler() {
    this.addHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    }, ClickEvent.getType());
  }

  /**
   * マウスアウト時ハンドラの設定
   * 
   * @return
   */
  private MouseOutHandler setMouseOutHanlder() {
    return new MouseOutHandler() {
      @Override
      public void onMouseOut(MouseOutEvent event) {
        hide();
      }
    };
  }

  /**
   * パネルの表示内容設定
   * 
   * @param activity
   */
  private void setPanel(PlusActivity activity) {
    this.clear();
    published.setText(Formatter.getYYMDString(activity.getPublished()));
    accessDescription.setText(activity.getAccessDescription());
    title.setText(activity.getTitle());
    VerticalPanel innerPanel = new VerticalPanel();
    image = getImage(activity);
    innerPanel.add(image);
    innerPanel.add(published);
    innerPanel.add(accessDescription);
    innerPanel.add(title);
    this.add(innerPanel);
  }

  /**
   * アクテビティ写真オブジェクトの取得
   * 
   * @param activity
   * @return
   */
  private Image getImage(PlusActivity activity) {
    image = new Image();
    if (activity.getAttachmentImageUrls().size() > 0) {
      image.setUrl(activity.getAttachmentImageUrls().get(0));
    }
    return image;
  }

  /**
   * ウィンドウの表示
   */
  public void show(PlusActivity activity, SquareDimensions photoDimensions,
      SquareDimensions clickPosition) {
    setPanel(activity);
    setPosition(activity, photoDimensions, clickPosition);
    super.show();
  }

  /**
   * 表示位置を設定する
   * 
   * @param activity
   */
  private void setPosition(PlusActivity activity, SquareDimensions photoDimensions,
      SquareDimensions clickPosition) {
    int posX, posY;
    photoDimensions = calcUtil.optimizeDetailPhotoDimensions(parentPanelDimensions,
        photoDimensions);
    image.setWidth(Statics.getLengthWithUnit((int) photoDimensions.getWidth()));
    image.setHeight(Statics.getLengthWithUnit((int) photoDimensions.getHeight()));
    posX = (int) (parentPanelDimensions.getWidth() / 2 - photoDimensions.getWidth() / 2);
    posY = (int) (clickPosition.getHeight());
    this.setWidth(Statics.getLengthWithUnit((int) photoDimensions.getWidth()));
    this.setPopupPosition(posX, posY);
  }

}
