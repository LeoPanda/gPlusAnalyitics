package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;

/**
 * ページコントロール用のパネルパーツを作成する
 * 
 * @author LeoPanda
 *
 */
public abstract class PageControlLine extends HorizontalPanel {
  // ボタン
  private Button firstPageButton = new Button("◀◀");
  private Button lastPageButton = new Button("▶▶");
  private Button prevPageButton = new Button("◀");
  private Button nextPageButton = new Button("▶");
  // ボタンの間のフリースペース
  private HorizontalPanel spaceOfPageControl = new HorizontalPanel();
  public HorizontalPanel getFreeSpace(){
    return this.spaceOfPageControl;
  }
  // ボタンクリック時イベントの抽象メソッド
  abstract public void onFirstPageButtonClick(ClickEvent event);

  abstract public void onLastPageButtonClick(ClickEvent event);

  abstract public void onPrevPageButtonClick(ClickEvent event);

  abstract public void onNextPageButtonClick(ClickEvent event);

  /**
   * コンストラクタ
   * @param width ラインの幅を指定する
   */
  public PageControlLine(String width) {
    firstPageButton.addStyleName(MyStyle.PAGE_BUTTON.getStyle());
    prevPageButton.addStyleName(MyStyle.PAGE_BUTTON.getStyle());
    nextPageButton.addStyleName(MyStyle.PAGE_BUTTON.getStyle());
    lastPageButton.addStyleName(MyStyle.PAGE_BUTTON.getStyle());
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
    
    addButtonClickHandlers();
  }

  /**
   * ボタンクリック時ハンドラの実装 
   */
  private void addButtonClickHandlers() {
    // 先頭ページボタン
    firstPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onFirstPageButtonClick(event);
      }
    });
    // 最終ページボタン
    lastPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onLastPageButtonClick(event);
      }

    });
    // 前ページボタン
    prevPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onPrevPageButtonClick(event);
      }
    });
    // 次ページボタン
    nextPageButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onNextPageButtonClick(event);
      }
    });

  }

}
