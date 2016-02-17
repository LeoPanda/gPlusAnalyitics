package jp.leopanda.gPlusAnalytics.server;

import java.util.Date;
import java.util.List;

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
public class JsonDecoder<T1 extends PlusItemList<T2>,T2 extends PlusItem> {
	String requestUri;
	String oAuthToken;
	T1 resultData;
	List<T2> resultDataItems;
	boolean testBit = false;//trueに設定するとAPIの読み込みを１ページ分だけに制限する
	/**
	 * コンストラクタ
	 * 
	 * @param requestUri　API 呼び出しのURI
	 * @param oAuthToken  oAuth2 認証トークン
	 */
	JsonDecoder(String requestUri, String oAuthToken) {
		this.requestUri = requestUri;
		this.oAuthToken = oAuthToken;
	}

	/**
	 * 複数のページに渡って分割された一覧データをすべて取得し、指定されたdata object classへ格納して返す。
	 * 
	 * @param targetClass
	 * @return
	 * @throws HostGateException
	 */
	public T1 decode(Class<T1> targetClass) throws HostGateException {
		resultData = JSON.decode(getHttpResult(null), targetClass);
		String pageToken = testBit ? null : resultData.getNextPageToken();
		resultDataItems = resultData.getItems();
		while (pageToken != null) {
			T1 nextPage = JSON.decode(getHttpResult(pageToken), targetClass);
			if (nextPage.getItems() != null) {
				resultDataItems = nextPage.deposit(resultDataItems);
			}
			pageToken = nextPage.getNextPageToken();
		}
		resultData.setItems(resultDataItems);
		return resultData;
	}
	/**
	 * 複数のページに渡って分割された一覧データをすべて取得し、指定されたdata object classへ格納して返す。
	 * 指定された日付以降に更新されたページのみを取得する。
	 * @param targetClass
	 * @param updated
	 * @return
	 * @throws HostGateException
	 */
	public T1 decodeLaterUpdatedPage(Class<T1> targetClass,Date updated) throws HostGateException{
		resultData = JSON.decode(getHttpResult(null), targetClass);
		String pageToken = testBit ? null : resultData.getNextPageToken();
		resultDataItems = resultData.getItems();
		while (pageToken != null) {
			T1 nextPage = JSON.decode(getHttpResult(pageToken), targetClass);
			if(updated !=null){
				if(nextPage.getUpdated().before(updated)){
					break;
				}				
			}
			if (nextPage.getItems() != null) {
				resultDataItems = nextPage.deposit(resultDataItems);
			}
			pageToken = nextPage.getNextPageToken();
		}
		resultData.setItems(resultDataItems);
		return resultData;
		
	}
	/**
	 * クラス生成時に指定されたURIのレスポンスを得る
	 * 次ページトークンが指定された場合はこれを追加する
	 * @param pageToken
	 * @return
	 * @throws HostGateException
	 */
	private String getHttpResult(String pageToken) throws HostGateException {
		UrlService urlService = new UrlService();
		String urlStr = requestUri
				+(testBit ? "?maxResults=10" : "")
				+ (pageToken == null ? "" : "?pageToken=" + pageToken);
		Results results = urlService.fetchGet(
				urlStr,
				urlService.addToken(oAuthToken,
						urlService.setHeader(ContentType.JSON)));

		if (results.getRetCode() != HttpStatus.SC_OK) {
			throw new HostGateException(String.valueOf(results.getRetCode()));
		}

		return results.getBody();
	}

}
