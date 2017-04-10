package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Grid;

import jp.leopanda.gPlusAnalytics.client.util.Divider;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティ写真一覧の行を作成する
 * 
 * @author LeoPanda
 *
 */
public class PhotoGridLine extends ArrayList<PlusActivity> {

  private static final long serialVersionUID = 1L;
  private Divider divider = new Divider();
  private int gridHeight;
  
  public PhotoGridLine(int gridHeight){
    this.gridHeight = gridHeight;
  }

  /**
   * アクテビティをグリッドに追加する 
   * グルーピングしきい値が変化したらFalseを返して追加しない。
   * 
   * @param activity
   * @param groupBy
   * @return
   */
  public boolean addGridByDivider(PlusActivity activity,String groupBy) {
    boolean agregateCheck = divider.checkAgregate(groupBy);
    if (agregateCheck) {
      this.add(activity);
    }
    return agregateCheck;
  }

  /**
   * グルーピングのしきい値を得る
   * 
   * @return
   */
  public String getGroupBy() {
    return this.divider.getGroupBy();
  }

  /*
   * ラインをクリアする
   */
  @Override
  public void clear() {
    super.clear();
    divider = new Divider();
  }

  /**
   * 写真一覧のグリッド行を取得する
   * 
   * @return
   */
  public Grid getGrid() {

    if (this.size() == 0) {
      return null;
    }
    Grid grid = new Grid(1, this.size());
    int cellIndex = 0;
    for (PlusActivity activity : this) {
      grid.setWidget(0, cellIndex++, new PhotoGridCell(activity,gridHeight));
    }
//    for (int cellIndex = 0; cellIndex < numOfCell; cellIndex++) {
//      grid.setWidget(0, cellIndex, new PhotoGridCell(this.get(cellIndex)));
//    }
    return grid;
  }

}
