package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.People.ListByActivity;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import com.google.appengine.api.utils.SystemProperty;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.googleAuthorization.server.CredentialUtils;

import com.google.api.services.plus.model.PeopleFeed;

/**
 * Google+ API client ライブラリーを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class PlusApiService {
  private final String collectionPublic = "public";
  private final String collectionPlusoners = "plusoners";
  private Plus plus;

  private DeadLineChecker checker = new DeadLineChecker();

  private final String activityFields = "items(access/description,actor(id,displayName,url),id,kind,title,updated,url,object(attachments,content,plusoners))";
  private final String personFields = "items(displayName,id,image/url,url)";
  private final String nextPageToken = ",nextPageToken";

  /**
   * コンストラクタ GWT RPC用
   * 
   * @param credential
   * @throws Exception
   */
  public PlusApiService(CredentialUtils credential) throws Exception {
    this.plus = getPlus(credential.httpTransport, credential.jsonFactory,
        credential.loadCredential());
  }
  /**
   * コンストラクタ cron用
   * @param httpTransport
   * @param jsonFactory
   * @param credential
   * @throws Exception
   */
  public PlusApiService(HttpTransport httpTransport,JsonFactory jsonFactory,Credential credential) throws Exception {
    this.plus = getPlus(httpTransport, jsonFactory,credential);
  }

  Logger logger = Logger.getLogger("PlusApiService");

  /**
   * Google Plus APIオブジェクト取得
   * 
   * @param credential
   *          Google認証
   * @return Plus Google Plus APIオブジェクト取得
   * @throws IOException
   */
  private Plus getPlus(HttpTransport transport, JsonFactory jsonFactory,
      Credential credential) throws IOException {
    Plus plus = new Plus.Builder(transport, jsonFactory, credential)
        .setApplicationName(SystemProperty.applicationId.get())
        .build();
    return plus;
  }

  /**
   * Google+ ユーザーIDの取得
   * 
   * @return String ユーザーID
   * @throws IOException
   */
  public String getGplusUserId() throws IOException {
    Person user = plus.people().get("me").execute();
    return user.getId();
  }

  /**
   * Google plus 全アクティビティのリスト取得
   * 
   * @param userId
   *          String ユーザーID
   * @return List<PlusActivity> アクティビティ
   * @throws IOException
   *           IO例外
   */
  public List<PlusActivity> getPlusActivies(String userId) throws Exception {
    List<PlusActivity> activities = new ArrayList<PlusActivity>();
    PlusActivityMaker activityMaker = new PlusActivityMaker();
    Plus.Activities.List listActivities = null;
    listActivities = plus.activities().list(userId, collectionPublic)
        .setFields(activityFields + nextPageToken).setMaxResults(100L);
    ActivityFeed feed = null;
    String nextPageToken = "";
    while (nextPageToken != null) {
      checker.checkRuntimeExceed();
      feed = listActivities.execute();
      for (Activity activity : feed.getItems()) {
        activities.add(activityMaker.generate(activity));
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
      throws Exception {
    List<PlusPeople> plusPeople = new ArrayList<PlusPeople>();
    PlusPeopleMaker plusPeopleMaker = new PlusPeopleMaker();
    ListByActivity listPeople = plus.people().listByActivity(activityId, collectionPlusoners)
        .setMaxResults(100L).setFields(personFields + nextPageToken);
    PeopleFeed feed = null;
    String nextPageToken = "";
    while (nextPageToken != null) {
      checker.checkRuntimeExceed();
      feed = listPeople.execute();
      for (Person person : feed.getItems()) {
        plusPeople.add(plusPeopleMaker.get(person));
      }
      nextPageToken = feed.getNextPageToken();
      listPeople.setPageToken(nextPageToken);
    }
    return plusPeople;
  }
  
  /**
   * Google Plus 単一アクテビティの取得
   * 
   * @param activityId
   * @return
   * @throws Exception
   */
  public PlusActivity getPlusActiviy(String activityId) throws Exception {
    Activity activity = null;
    try {
      activity = plus.activities().get(activityId).setFields(activityFields).execute();
    } catch (IOException e) {
      if (e instanceof GoogleJsonResponseException) {
        int statusCode = ((GoogleJsonResponseException) e).getStatusCode();
        if (statusCode == HttpStatus.SC_NOT_FOUND) {
          logger.info("activity not exist in google+ any more.");
          return null;
        } else {
          e.printStackTrace();
          throw new IOException(e);
        }
      }
    }
    if (activity == null) {
      return null;
    }
    return new PlusActivityMaker().generate(activity);
  }

}
