/**
 * 
 */
package jp.leopanda.gPlusAnalytics.interFace;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ REST API アクテビティ一覧取得用リスナー
 * @author LeoPanda
 *
 */
public interface PlusActivityListListener {
	public void onCallback(List<PlusActivity> activityList);

}
