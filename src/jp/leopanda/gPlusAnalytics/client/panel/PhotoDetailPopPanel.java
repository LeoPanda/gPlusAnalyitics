package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
 * 写真詳細ポップアップ画面
 * 
 * @author LeoPanda
 *
 */
public class PhotoDetailPopPanel extends PopupPanel {
  Label published = new Label();
  Label numOfPlusOne = new Label();
  Label title = new Label();
  Label accessDescription = new Label();
  Image image;

  PhotoCalcUtil calcUtil = new PhotoCalcUtil();
  SquareDimensions parentPanelDimensions;

  /**
   * コンストラクタ
   * 
   * @param activity
   * @param posX
   * @param posY
   */
  public PhotoDetailPopPanel(SquareDimensions parentPanelDimensions) {
    super();
    this.parentPanelDimensions = parentPanelDimensions;
    setStyle();
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
    title.setText(activity.getTitle());
    numOfPlusOne.setText("+1:" + String.valueOf(activity.getNumOfPlusOners()));
    accessDescription.setText(activity.getAccessDescription());
    HorizontalPanel publishedLine = new HorizontalPanel();
    publishedLine.add(published);
    publishedLine.add(new HTML("<BR>"));
    publishedLine.add(numOfPlusOne);
    VerticalPanel innerPanel = new VerticalPanel();
    innerPanel.add(getImage(activity));
    innerPanel.add(publishedLine);
    innerPanel.add(accessDescription);
    innerPanel.add(title);
    this.add(innerPanel);
  }

  /**
   * スタイルの設定
   */
  private void setStyle() {
    accessDescription.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    title.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    this.addStyleName(MyStyle.DETAIL_POPWINDOW.getStyle());
  }

  /**
   * アクテビティ写真オブジェクトの取得
   * 
   * @param activity
   * @return
   */
  private Image getImage(PlusActivity activity) {
    image = new Image();
    activity.getAttachmentImageUrls().ifPresent(urls -> image.setUrl(urls.get(0)));
    return image;
  }

  /**
   * ウィンドウの表示
   */
  public void show(PlusActivity activity, SquareDimensions photoDimensions, int clickX,
      int clickY) {
    setPanel(activity);
    setPosition(activity, photoDimensions, clickX, clickY);
    super.show();
  }

  /**
   * 表示位置を設定する
   * 
   * @param activity
   */
  private void setPosition(PlusActivity activity, SquareDimensions photoDimensions,
      int clickX, int clickY) {
    int posX, posY;
    photoDimensions = calcUtil.optimizeDetailPhotoDimensions(parentPanelDimensions,
        photoDimensions);
    image.setWidth(Statics.getLengthWithUnit((int) photoDimensions.getWidth()));
    image.setHeight(Statics.getLengthWithUnit((int) photoDimensions.getHeight()));
    posX = (int) (clickX - photoDimensions.getWidth() / 2);
    posY = (int) (clickY - photoDimensions.getHeight() / 2);
    posX = posX < 0 ? 0 : posX;
    posY = posY < 0 ? 0 : posY;

    this.setWidth(Statics.getLengthWithUnit((int) photoDimensions.getWidth()));
    this.setPopupPosition(posX, posY);
  }

}
