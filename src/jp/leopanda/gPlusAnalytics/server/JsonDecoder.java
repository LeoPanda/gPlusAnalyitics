package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import net.arnx.jsonic.JSON;
import jp.leopanda.common.server.Results;
import jp.leopanda.common.server.UrlService;
import jp.leopanda.common.server.UrlService.ContentType;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItemList;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

/**
 * Google+ RPC APIからのJSONレスポンスをData Objectに変換するユーティリティクラス
 * 
 * @author LeoPanda
 *
 */
public class JsonDecoder<L extends PlusItemList<I>, I extends PlusItem> {
	String requestUri;
	String oAuthToken;
	L resultData;
	List<I> resultDataItems;
	boolean testBit = false;// trueに設定するとAPIの読み込みを１ページ分だけに制限する
	int retryCount = 3;

	Logger logger = Logger.getLogger("JsonDecoder");

	/**
	 * コンストラクタ
	 * 
	 * @param requestUri
	 *            　API 呼び出しのURI
	 * @param oAuthToken
	 *            oAuth2 認証トークン
	 */
	JsonDecoder(String requestUri, String oAuthToken) {
		this.requestUri = requestUri;
		this.oAuthToken = oAuthToken;
	}
	/**
	 * １ページ分だけデータを読む
	 * 
	 * @param targetClass
	 * @return
	 * @throws HostGateException
	 */
	public L decodeOnePage(Class<L> targetClass) throws HostGateException {
		boolean saveBit = testBit;
		testBit = true;
		L result = decode(targetClass);
		testBit = saveBit;
		return result;
	}
	/**
	 * 複数のページに渡って分割された一覧データをすべて取得し、指定されたdata object classへ格納して返す。
	 * 
	 * @param resultClass
	 * @return
	 * @throws HostGateException
	 */
	public L decode(Class<L> targetClass) throws HostGateException {
		resultData = JSON.decode(getHttpResult(null), targetClass);
		String pageToken = testBit ? null : resultData.getNextPageToken();
		resultDataItems = resultData.getItems();
		while (pageToken != null) {
			L nextPage = JSON.decode(getHttpResult(pageToken), targetClass);
			if (nextPage.getItems() != null) {
				resultDataItems = nextPage.deposit(resultDataItems);
			}
			pageToken = nextPage.getNextPageToken();
		}
		resultData.setItems(resultDataItems);
		return resultData;
	}
	// /**
	// * 複数のページに渡って分割された一覧データをすべて取得し、指定されたdata object classへ格納して返す。
	// * 指定された日付以降に更新されたページのみを取得する。
	// *
	// * @param resultClass
	// * @param updated
	// * @return
	// * @throws HostGateException
	// */
	// public T1 decodeLaterUpdatedPage(Class<T1> targetClass, Date updated)
	// throws HostGateException {
	// resultData = JSON.decode(getHttpResult(null), targetClass);
	// String pageToken = testBit ? null : resultData.getNextPageToken();
	// resultDataItems = resultData.getItems();
	// T1 nextPage = resultData;
	// while (pageToken != null) {
	// if (updated != null) {
	// if (nextPage.getUpdated().before(updated)) {
	// break;
	// }
	// }
	// nextPage = JSON.decode(getHttpResult(pageToken), targetClass);
	// if (nextPage.getItems() != null) {
	// resultDataItems = nextPage.deposit(resultDataItems);
	// }
	// pageToken = nextPage.getNextPageToken();
	// }
	// resultData.setItems(resultDataItems);
	// return resultData;
	//
	// }
	/**
	 * クラス生成時に指定されたURIのレスポンスを得る 次ページトークンが指定された場合はこれを追加する
	 * 
	 * @param pageToken
	 * @return
	 * @throws HostGateException
	 */
	private String getHttpResult(String pageToken) throws HostGateException {
		UrlService urlService = new UrlService();
		String connectionChar = getConnectionChar();
		String urlStr = requestUri
				+ (testBit ? connectionChar + "maxResults=10" : "")
				+ (pageToken == null ? "" : connectionChar + "pageToken="
						+ pageToken);
		Results results = urlService.fetchGet(
				urlStr,
				urlService.addToken(oAuthToken,
						urlService.setHeader(ContentType.JSON)));

		if (results.getRetCode() != HttpStatus.SC_OK) {
			if(results.getRetCode() == HttpStatus.SC_SERVICE_UNAVAILABLE){
				if(retryCount >0){
				retryCount -= 1;
				logger.log(Level.WARNING,"Server is busy,retry.");
				logger.log(Level.SEVERE, "requestedUrl=" + urlStr);
				pause(100);
				return getHttpResult(pageToken);
				}else{
					logger.log(Level.SEVERE,"retry count over.");
					throw new HostGateException(String.valueOf(results.getRetCode()));					
				}
			}
			logger.log(Level.SEVERE,
					"HttpCode=" + String.valueOf(results.getRetCode()));
			logger.log(Level.SEVERE, "requestedUrl=" + urlStr);
			logger.log(Level.SEVERE, "return=" + results.getBody());
			throw new HostGateException(String.valueOf(results.getRetCode()));
		}

		return results.getBody();
	}
	/**
	 * パラメータの接続文字列を得る
	 * 
	 * @return
	 */
	private String getConnectionChar() {
		String result;
		if (requestUri.contains("?")) {
			result = "&";
		} else {
			result = "?";
		}
		return result;
	}
	/**
	 * wait for milliseconds
	 * @param millis
	 */
	private static void pause(int millis) {
	    try {
	        Thread.sleep(millis);
	    } catch (InterruptedException e) {
	        // Do nothing
	    }
	}
}
