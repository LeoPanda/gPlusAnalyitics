package jp.leopanda.gPlusAnalytics.interFace;

import jp.leopanda.gPlusAnalytics.client.util.SquareDimensions;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 詳細ウィンドウの表示リクエストを通知するリスナー
 * @author LeoPanda
 *
 */
public interface DetailPopRequestListener {
  void request(PlusActivity activity,SquareDimensions photoDimensions,SquareDimensions clickPosition);

}
