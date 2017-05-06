package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.DetailPopRequestListener;

/**
 * アクテビティ写真の全リスト
 * 
 * @author LeoPanda
 *
 */
public class PhotoCellList extends ArrayList<PhotoCell> {

  private static final long serialVersionUID = 1L;

  /**
   * コンストラクタ
   */
  public PhotoCellList() {
  }


  /**
   * アクテビティをリストに追加する
   * 
   * @param activity
   * @param gridHeight
   * @param detailPopRequestListener
   */
  public void addCell(PlusActivity activity,int gridHeight,DetailPopRequestListener detailPopRequestListener) {
      this.add(new PhotoCell(activity, gridHeight, detailPopRequestListener));
  }
}
