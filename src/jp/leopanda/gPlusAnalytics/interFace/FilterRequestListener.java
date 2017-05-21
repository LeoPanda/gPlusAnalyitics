package jp.leopanda.gPlusAnalytics.interFace;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;

/**
 * フィルター入力パネルからのフィルターリクエストイベントを通知する
 * @author LeoPanda
 *
 */
public interface FilterRequestListener {
  void request(FilterType filterType,Object keyword);
}
