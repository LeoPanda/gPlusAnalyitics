package jp.leopanda.gPlusAnalytics.server;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * GoogleAPIを使用するための基本オブジェクトを作成する
 * 
 * @author LeoPanda
 *
 */
public class GoogleApiCore {
  private UrlFetchTransport urlFetchTransport = new UrlFetchTransport();
  private JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
  private Credential credential;

  /**
   * 認証を生成しないコンストラクタ
   */
  public GoogleApiCore() {
    this.credential = null;
  }

  /**
   * oAuth認証によるコンストラクタ
   * 
   * @param oauthToken
   *          String 認証トークン
   */
  public GoogleApiCore(String oauthToken) {
    this.credential = getCredentialByToken(oauthToken);
  }

  /**
   * キーファイル認証によるコンストラクタ
   * 
   * @param p12key
   *          File 認証キー
   * @param emailAdress
   *          String サービスアカウントID
   * @return GoogleCredential Google認証オブジェクト
   * @throws GeneralSecurityException
   *           認証れ～概
   * @throws IOException
   *           IO例外
   */
  public GoogleApiCore(File p12key, String emailAdress, String scope)
      throws GeneralSecurityException, IOException {
    this.credential = getCredentialByP12(p12key, emailAdress, scope);
  }

  /**
   * Google API用トランスポートを取得する
   * @return UrlFetchTransport
   */
  public UrlFetchTransport getTransport() {
    return this.urlFetchTransport;
  }

  /**
   * GoogleAPI用　JacksonFactoryを取得する
   * @return　JacksonFactory
   */
  public JacksonFactory getJacksonFactory() {
    return this.jacksonFactory;
  }

  /**
   * GoogleAPI用認証オブジェクトを取得する
   * @return　Credential
   */
  public Credential getCredential() {
    return this.credential;
  }

  /**
   * oAuth認証
   * 
   * @param oauthToken
   *          String 認証トークン
   * @return Credential
   */
  private Credential getCredentialByToken(String oauthToken) {
    return new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(oauthToken)
        .setExpiresInSeconds(3600L);
  }

  /**
   * キーファイル認証生成
   * 
   * @param p12key
   *          File 認証キー
   * @param emailAdress
   *          String サービスアカウントID
   * @return GoogleCredential Google認証オブジェクト
   * @throws GeneralSecurityException
   *           認証れ～概
   * @throws IOException
   *           IO例外
   */
  private GoogleCredential getCredentialByP12(File p12key, String emailAdress, String scope)
      throws GeneralSecurityException, IOException {
    GoogleCredential credential = new GoogleCredential.Builder().setTransport(urlFetchTransport)
        .setJsonFactory(jacksonFactory)
        .setServiceAccountId(emailAdress)
        .setServiceAccountScopes(Collections.singleton(scope))
        .setServiceAccountPrivateKeyFromP12File(p12key)
        .build();
    return credential;
  }

}
