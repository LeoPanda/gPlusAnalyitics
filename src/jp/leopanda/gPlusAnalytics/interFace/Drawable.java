package jp.leopanda.gPlusAnalytics.interFace;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * チャートメニュー搭載用インターフェース
 * 
 * @author LeoPanda
 *
 */
public interface Drawable<I extends PlusItem> {
  public void draw();

  public void reDraw();

  public void resetChart();

  public void setSourceData(List<I> sourceData);

  public void setOutlineSpec(String chartTitile, int chartWidth, int chartHeight);
}
