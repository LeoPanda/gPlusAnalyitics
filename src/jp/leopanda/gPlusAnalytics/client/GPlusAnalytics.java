package jp.leopanda.gPlusAnalytics.client;

import jp.leopanda.gPlusAnalytics.client.enums.CallFunction;
import jp.leopanda.gPlusAnalytics.client.enums.Images;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.panel.MainPanel;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.interFace.RpcGateListener;
import jp.leopanda.googleAuthorization.client.Auth;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GPlusAnalytics implements EntryPoint {
  // 画面構成要素のメンバ変数
  private HorizontalPanel outerPanel; // 外枠
  private final String outerPanelName = "outerPanel"; // HTMLのdiv名に合わせる

  /**
   * メイン処理
   */
  public void onModuleLoad() {
    outerPanel = new HorizontalPanel();
    outerPanel.add(getMessagePanel("Getting Authorize...", Images.GEARS));
    RootPanel.get(outerPanelName).add(outerPanel);

    // oAuth2認証取得リクエスト
    @SuppressWarnings("unused")
    Auth auth = new Auth() {

      @Override
      public void onGetToken() {
        // 認証取得後の処理
        requestDataLoad();
      }

    };
  }

  /**
   * 認証取得後、サーバーサイドデータの取得をリクエストする
   */
  private void requestDataLoad() {
    outerPanel.clear();
    outerPanel.add(getMessagePanel("loading Data...", Images.LOADING));

    new RpcGate<SourceItems>(CallFunction.GET_STOREDITEMS, getOnDataLoad()) {}.request();
  }

  /**
   * データロード後の処理
   * 
   * @return
   */
  private RpcGateListener<SourceItems> getOnDataLoad() {
    return result -> {
      outerPanel.clear();
      outerPanel.add(new MainPanel(result.activities, result.plusOners));
    };
  }

  /**
   * 待機メッセージを表示するパネルを生成する
   * 
   * @param message
   * @param style
   * @return
   */
  private VerticalPanel getMessagePanel(String message, Images images) {
    VerticalPanel messagePanel = new VerticalPanel();
    Label messageLabel = new Label(message);
    messagePanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
    messageLabel.addStyleName(MyStyle.MESSAGE_LABEL.getStyle());
    messagePanel.add(messageLabel);
    messagePanel.add(getIconImage(images));
    return messagePanel;
  }

  /**
   * 待機メッセージのアイコンイメージを生成する
   * @param images
   * @return
   */
  private Image getIconImage(Images images){
    Image image = new Image(images.get());
    image.setWidth(Statics.getLengthWithUnit(60));
    image.setHeight(Statics.getLengthWithUnit(60));
    return image;
  }
  
}
