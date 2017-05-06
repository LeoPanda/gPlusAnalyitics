package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Date;

/**
 * 写真パネル用
 * ページをまたがり継続するべき変数をスタックする
 * @author LeoPanda
 *
 */
public class PhotoPanelPageKeeper {

  int currentIndex;
  Date currentPublishDate;
  PhotoPanelDisplayCell currentCell;
  
  /**
   * コンストラクタ（初期用）
   */
  public PhotoPanelPageKeeper(){
    clear();
  }
  /**
   * コンストラクタ(引き継ぎ作成用)
   * @param currentIndex
   * @param currentPublishDate
   * @param currentCell
   */
  public PhotoPanelPageKeeper(int currentIndex,Date currentPublishDate,PhotoPanelDisplayCell currentCell){
    this.currentIndex = currentIndex;
    this.currentPublishDate = currentPublishDate;
    this.currentCell = currentCell;
  }

  /**
   * 初期化する 
   */
  public void clear(){
    currentIndex = 0;
    currentPublishDate = null;
    currentCell = null;
  }

  
  //getter
  public int getCurrentIndex() {
    return currentIndex;
  }

  public Date getCurrentPublishDate() {
    return currentPublishDate;
  }

  public PhotoPanelDisplayCell getCurrentCell() {
    return currentCell;
  }
  
}
