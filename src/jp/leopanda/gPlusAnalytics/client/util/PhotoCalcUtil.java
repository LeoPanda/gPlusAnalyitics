package jp.leopanda.gPlusAnalytics.client.util;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 写真オブジェクト用計算ユーティリティ
 * 
 * @author LeoPanda
 *
 */
public class PhotoCalcUtil {
  final double defaultPhotoRatio = (double) 4 / 3;
  final double lengthOcupancy = 0.6;

  /**
   * グリッド上での単体写真幅を計算する
   * 
   * @param activity
   * @param gridHeight
   * @return
   */
  public double getPhotoWidthOnGrid(PlusActivity activity, long gridHeight) {
    return (double) (getActivityPhotoRatio(activity) * gridHeight);
  }

  /**
   * 写真オブジェクトの横縦比率を取得する
   * 
   * @param activity
   * @return
   */
  private double getActivityPhotoRatio(PlusActivity activity) {
    double photoRatio = 0;
    switch (attachmentsExsists(activity)) {
    case EXSISTS:
      long imgWidth = activity.object.attachments.get(0).getImageWidth();
      long imgHeight = activity.object.attachments.get(0).getImageHeight();
      photoRatio = (double) imgWidth / imgHeight;
      break;
    case HAS_NO_SIZE:
      photoRatio = defaultPhotoRatio;
    case NOT_EXSISTS:
      photoRatio = 0;
      break;
    default:
      break;
    }
    return photoRatio;
  }

  /**
   * アクテビティに写真オブジェクトが存在するかどうかをチェックする
   * 
   * @param activity
   * @return
   */
  private AttachmentsStatus attachmentsExsists(PlusActivity activity) {
    AttachmentsStatus status;
    if (activity.object.attachments.size() > 0) {
      if (activity.object.attachments.get(0).getImageHeight() == null
          || activity.object.attachments.get(0).getImageWidth() == null) {
        status = AttachmentsStatus.HAS_NO_SIZE;
      } else {
        status = AttachmentsStatus.EXSISTS;
      }
    } else {
      status = AttachmentsStatus.NOT_EXSISTS;
    }
    return status;
  }

  /**
   * グリッド詳細画面写真の縦横寸法を最適化する
   * 
   * @param activity
   * @param parentPanelDimensions
   * @return
   */
  public SquareDimensions optimizeDetailPhotoDimensions(SquareDimensions parentPanelDimensions,
      SquareDimensions photoDimensions) {
    double width = 0;
    double height = 0;
    double photoRatio = (double) (photoDimensions.getWidth() / photoDimensions.getHeight());
    switch (getPhotoOrientation(photoRatio)) {
    case HORIZONTAL:
      width = (double) parentPanelDimensions.getWidth() * lengthOcupancy;
      height = (double) width / photoRatio;
      break;
    case VERTICAL:
      height = (double) parentPanelDimensions.getHeight() * lengthOcupancy;
      width = (double) height * photoRatio;
    default:
      break;
    }
    return new SquareDimensions(width, height);
  }

  /**
   * 写真の縦横方向を判定する
   * 
   * @param photoRatio
   * @return
   */
  private PhotoOrientation getPhotoOrientation(double photoRatio) {
    if (photoRatio < 1) {
      return PhotoOrientation.VERTICAL;
    }
    return PhotoOrientation.HORIZONTAL;
  }

  /**
   * 写真の向き（縦横）
   * 
   * @author LeoPanda
   *
   */
  private enum PhotoOrientation {
    HORIZONTAL, VERTICAL;
  }

  /**
   * 写真オブジェクトの存在ステータス
   * 
   * @author LeoPanda
   *
   */
  private enum AttachmentsStatus {
    EXSISTS, HAS_NO_SIZE, NOT_EXSISTS;
  }
}
