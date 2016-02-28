package jp.leopanda.gPlusAnalytics.interFace;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * Google+ REST API プラスワンユーザー一覧取得用リスナー
 * 
 * @author LeoPanda
 *
 */
public interface PlusOnersListListner {
  public void onCallback(List<PlusPeople> peopleList);
}
