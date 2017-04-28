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

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティ詳細ポップアップ画面
 * 
 * @author LeoPanda
 *
 */
public class ActivityDetailPop extends PopupPanel {
  PlusActivity activity;

  /**
   * コンストラクタ
   * 
   * @param activity
   * @param posX
   * @param posY
   */
  public ActivityDetailPop(PlusActivity activity, int posX, int posY) {
    super();
    this.activity = activity;
    setPosition(posX, posY);
    sinkEvents(Event.ONCLICK);
    sinkEvents(Event.ONMOUSEOUT);
    addDomHandler(setMouseOutHanlder(), MouseOutEvent.getType());
    setClickHandler(activity);

    setPanel(activity);
    show();
  }

  /**
   * クリックハンドラの設定
   * 
   * @param activity
   */
  private void setClickHandler(final PlusActivity activity) {
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
    Label published = new Label(Formatter.getYYMDString(activity.getPublished()));
    Label accessDescription = new Label(activity.getAccessDescription());
    Label title = new Label(activity.getTitle());
    VerticalPanel innerPanel = new VerticalPanel();
    innerPanel.add(getImage(activity));
    innerPanel.add(published);
    innerPanel.add(accessDescription);
    innerPanel.add(title);
    published.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    accessDescription.addStyleName(MyStyle.DETAIL_POPLINE.getStyle());
    this.addStyleName(MyStyle.DETAIL_POPWINDOW.getStyle());
    this.add(innerPanel);
  }

  /**
   * アクテビティ写真オブジェクトの取得
   * 
   * @param activity
   * @return
   */
  private Image getImage(PlusActivity activity) {
    Image image = new Image();
    if (activity.getAttachmentImageUrls().size() > 0) {
      image.setUrl(activity.getAttachmentImageUrls().get(0));
      image.setWidth("300px");
    }
    return image;
  }

  /**
   * ウィンドウの再表示
   */
  public void reShow(int posX, int posY) {
    setPosition(posX, posY);
    show();
  }

  /**
   * 表示位置を設定する
   * 
   * @param posX
   * @param posY
   */
  private void setPosition(int clickedPosX, int clickedPosY) {
    int tryRePosX = clickedPosX - 150;
    int tryRePosY = clickedPosY - 100;
    int rePosX = tryRePosX > 0 ? tryRePosX : 10;
    int rePosY = tryRePosY > 0 ? tryRePosY : 10;
    this.setPopupPosition(rePosX, rePosY);
  }

}
