package jp.leopanda.gPlusAnalytics.client.util;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * @author LeoPanda
 *
 */
public class CalcUtil {
  final double defaultPhotoRatio = (double) 4 / 3;
  /**
   * グリッド上での単体写真幅を計算する
   * 
   * @param activity
   * @return
   */
  public double getPhotoWidth(PlusActivity activity,long gridHeight) {
    double photoRatio;
    if (activity.object.attachments.size() > 0) {
      if (activity.object.attachments.get(0).getImageHeight() == null
          || activity.object.attachments.get(0).getImageWidth() == null) {
        photoRatio = defaultPhotoRatio;
      } else {
        long imgWidth = activity.object.attachments.get(0).getImageWidth();
        long imgHeight = activity.object.attachments.get(0).getImageHeight();
        photoRatio = (double) imgWidth / imgHeight;
      }
    } else {
      photoRatio = 0;
    }
    double ret = (double) (photoRatio * gridHeight);
    return ret;

  }


}
