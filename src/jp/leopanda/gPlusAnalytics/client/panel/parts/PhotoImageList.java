package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.ArrayList;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.DetailPopRequestListener;

/**
 * アクテビティ写真の全リスト
 * PhotoPanelのパーツ
 *  PhotoPanel -- PhotoCellList -- PhotoCell
 * 
 * @author LeoPanda
 *
 */
public class PhotoImageList extends ArrayList<PhotoImage> {

  private static final long serialVersionUID = 1L;

  /**
   * コンストラクタ
   */
  public PhotoImageList() {
  }


  /**
   * イメージをリストに追加する
   * 
   * @param activity
   * @param gridHeight
   * @param detailPopRequestListener
   */
  public void addImage(PlusActivity activity,int gridHeight,DetailPopRequestListener detailPopRequestListener) {
      this.add(new PhotoImage(activity, gridHeight, detailPopRequestListener));
  }
}
