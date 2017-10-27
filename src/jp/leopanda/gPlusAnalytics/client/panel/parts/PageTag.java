package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.Date;

/**
 * 写真パネル用ページタグ
 * ページをまたがり継続するべき変数をスタックする
 * @author LeoPanda
 *
 */
public class PageTag {

  int currentIndex;
  Date currentPublishDate;
  PhotoCell currentCell;
  
  /**
   * コンストラクタ（初期用）
   */
  public PageTag(){
    clear();
  }

  /**
   * 初期化する 
   */
  public void clear(){
    currentIndex = 0;
    currentPublishDate = null;
    currentCell = null;
  }
  /**
   * setter
   * @param currentIndex
   * @param currentPublishDate
   * @param currentCell
   */
  public void set(int currentIndex,Date currentPublishDate,PhotoCell currentCell){
    this.currentIndex = currentIndex;
    this.currentPublishDate = currentPublishDate;
    this.currentCell = currentCell;
  }

  
  //getter
  public int getCurrentIndex() {
    return currentIndex;
  }

  public Date getCurrentPublishDate() {
    return currentPublishDate;
  }

  public PhotoCell getCurrentCell() {
    return currentCell;
  }
  
}
