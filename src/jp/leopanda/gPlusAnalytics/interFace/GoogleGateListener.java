package jp.leopanda.gPlusAnalytics.interFace;

import java.util.List;

/**
 * クライアント GoogleGate用リスナー
 * @author LeoPanda
 *
 */
public interface GoogleGateListener<T> {
	//値が返された時の処理
	public void onCallback(List<T> result);
}
