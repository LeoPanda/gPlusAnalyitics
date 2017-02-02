package jp.leopanda.gPlusAnalytics.server;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.People.ListByActivity;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import com.google.appengine.api.utils.SystemProperty;
import com.google.api.services.plus.model.PeopleFeed;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * Google+ API client ライブラリーを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class GoogleApiService {
  private static final String collectionPublic = "public";
  private static final String collectionPlusoners = "plusoners";
  private Plus plus;
  private UrlFetchTransport urlFetchTransport = new UrlFetchTransport();
  private JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
  private String applicationName = SystemProperty.applicationId.get();

  /**
   * コンストラクタ クライアントサイドでoauthToken取得
   * 
   * @param oauthToken
   *          String oAuth認証トークン
   */
  public GoogleApiService(String oauthToken) {
    this.plus = getPlus(getCredentialByToken(oauthToken));
  }

  /**
   * コンストラクタ サービスアカウント キーファイル認証
   * 
   * @param keyfile
   *          File 認証キー
   * @param emailAdress
   *          String サービスアカウントID
   * @throws IOException
   *           IO例外
   * @throws GeneralSecurityException
   *           認証例外
   */
  public GoogleApiService(File keyfile, String emailAdress)
      throws IOException, GeneralSecurityException {
    this.plus = getPlus(getCredentialByP12(keyfile, emailAdress));
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
  private GoogleCredential getCredentialByP12(File p12key, String emailAdress)
      throws GeneralSecurityException, IOException {
    GoogleCredential credential = new GoogleCredential.Builder().setTransport(urlFetchTransport)
        .setJsonFactory(jacksonFactory)
        .setServiceAccountId(emailAdress)
        .setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
        .setServiceAccountPrivateKeyFromP12File(p12key)
        .build();
    return credential;
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
   * Google Plus APIオブジェクト取得
   * 
   * @param credential
   *          Google認証
   * @return Plus Google Plus APIオブジェクト取得
   */
  private Plus getPlus(Credential credential) {
    Plus plus = new Plus.Builder(urlFetchTransport, jacksonFactory, credential)
        .setApplicationName(applicationName)
        .build();
    return plus;
  }

  /**
   * Google plus 全アクティビティのリスト取得
   * 
   * @param userId
   *          String ユーザー認証
   * @return List<PlusActivity> アクティビティ
   * @throws IOException
   *           IO例外
   */
  public List<PlusActivity> getPlusActivity(String userId)
      throws IOException {
    List<PlusActivity> activities = new ArrayList<PlusActivity>();
    ActivityMaker activityMaker = new ActivityMaker();
    Plus.Activities.List listActivities = plus.activities().list(userId, collectionPublic)
        .setMaxResults(40L);
    ActivityFeed feed = null;
    String nextPageToken = "";
    while (nextPageToken != null) {
      feed = listActivities.execute();
      for (Activity activity : feed.getItems()) {
        activities.add(activityMaker.get(activity));
      }
      nextPageToken = feed.getNextPageToken();
      listActivities.setPageToken(nextPageToken);
    }
    return activities;
  }

  /**
   * 特定アクティビティに＋１した全ユーザーのリスト取得
   * 
   * @param activityId
   *          String アクティビティID
   * @return List<PlusPeople> ユーザーリスト
   * @throws IOException
   *           IO例外
   */
  public List<PlusPeople> getPlusOnersByActivity(String activityId)
      throws IOException {
    List<PlusPeople> plusPeople = new ArrayList<PlusPeople>();
    PlusPeopleMaker plusPeopleMaker = new PlusPeopleMaker();
    ListByActivity listPeople = plus.people().listByActivity(activityId, collectionPlusoners)
        .setMaxResults(40L);
    PeopleFeed feed = null;
    String nextPageToken = "";
    while (nextPageToken != null) {
      feed = listPeople.execute();
      for (Person person : feed.getItems()) {
        plusPeople.add(plusPeopleMaker.get(person));
      }
      nextPageToken = feed.getNextPageToken();
      listPeople.setPageToken(nextPageToken);
    }
    return plusPeople;
  }
}
