package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.function.Consumer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.client.enums.Images;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;


/**
 * ページコントロール用のパネルパーツを作成する
 * 
 * @author LeoPanda
 *
 */
public class PageControlButtons extends HorizontalPanel {
  // ボタン
  private PushButton firstPageButton;
  private PushButton lastPageButton;
  private PushButton prevPageButton;
  private PushButton nextPageButton;

  // ボタンの間のフリースペース
  private HorizontalPanel spaceOfPageControl = new HorizontalPanel();
  
  /**
   * コンストラクタ
   * 
   * @param width ラインの幅を指定する
   */
  /**
   * @param width
   */
  public PageControlButtons(String width) {
    super();
    setPageControlButtons();
    setPanel(width);
  }

  /**
   * パネル配置をセットアップする
   * @param width
   */
  private void setPanel(String width) {
    this.setWidth(width);
    HorizontalPanel leftSide = new HorizontalPanel();
    HorizontalPanel rightSide = new HorizontalPanel();
    leftSide.add(firstPageButton);
    leftSide.add(prevPageButton);
    rightSide.add(nextPageButton);
    rightSide.add(lastPageButton);
    this.add(leftSide);
    this.setHorizontalAlignment(ALIGN_LEFT);
    this.add(spaceOfPageControl);
    this.setHorizontalAlignment(ALIGN_RIGHT);
    this.add(rightSide);
  }

  /**
   * ページコントロールボタンをセットアップする
   */
  private void setPageControlButtons() {
    firstPageButton = getPageControlButton(Images.REWARD_BUTTON);
    lastPageButton = getPageControlButton(Images.FORWARD_BUTTON);
    prevPageButton = getPageControlButton(Images.BACK_BUTTON);
    nextPageButton = getPageControlButton(Images.NEXT_BUTTON);
  }

  /**
   * ページコントロールボタンを生成する
   * @param images
   * @return
   */
  private PushButton getPageControlButton(Images images){
    PushButton button = new PushButton(getButtonImage(images));
    button.addStyleName(MyStyle.PAGE_BUTTON.getStyle());
    return button;
  }
  
  /**
   * ページコントロールボタンのイメージを生成する
   * @param images
   * @return
   */
  private Image getButtonImage(Images images){
    Image image = new Image(images.get());
    image.setWidth(Statics.getLengthWithUnit(24));
    image.setHeight(Statics.getLengthWithUnit(24));
    return image;
  }
  
  /**
   * ボタンの間のフリースペースを提供する
   * 
   * @return
   */
  public HorizontalPanel getFreeSpace() {
    return this.spaceOfPageControl;
  }

  /**
   * 最前ページボタンハンドラを設定する
   * 
   * @param handler
   */
  public void addFirstPageButtonClickHandler(Consumer<ClickEvent> handler) {
    firstPageButton.addClickHandler(event -> handler.accept(event));
  }

  /**
   * 最新ページボタンハンドラを設定する
   * 
   * @param handler
   */
  public void addlastPageButtonClickHandler(Consumer<ClickEvent> handler) {
    lastPageButton.addClickHandler(event -> handler.accept(event));
  }

  /**
   * 前ページボタンハンドラを設定する
   * 
   * @param handler
   */
  public void addPrevPageButtonClickHandler(Consumer<ClickEvent> handler) {
    prevPageButton.addClickHandler(event -> handler.accept(event));
  }

  /**
   * 次ページボタンハンドラを設定する
   * 
   * @param handler
   */
  public void addNextPageButtonClickHandler(Consumer<ClickEvent> handler) {
    nextPageButton.addClickHandler(event -> handler.accept(event));
  }

 
}
