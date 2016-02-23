package jp.leopanda.gPlusAnalytics.interFace;

/**
 * クライアント GoogleGate用リスナー
 * @author LeoPanda
 *
 */
public interface RpcGateListener<R> {
	//値が返された時の処理
	public void onCallback(R result);
}
