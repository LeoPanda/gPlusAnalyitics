package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.People.ListByActivity;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import com.google.appengine.api.utils.SystemProperty;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.api.services.plus.model.PeopleFeed;

/**
 * Google+ API client ライブラリーを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class PlusApiService {
  private static final String collectionPublic = "public";
  private static final String collectionPlusoners = "plusoners";
  private Plus plus;
  private String applicationName = SystemProperty.applicationId.get();

  /**
   * コンストラクタ クライアントサイドでoauthToken取得
   * 
   * @param credential  credential oAuth認証
   */
  public PlusApiService(GoogleApiCore apiCore) {
    this.plus = getPlus(apiCore);
  }
  /**
   * Google Plus APIオブジェクト取得
   * 
   * @param credential
   *          Google認証
   * @return Plus Google Plus APIオブジェクト取得
   */
  private Plus getPlus(GoogleApiCore apiCore) {
    Plus plus = new Plus.Builder(apiCore.getTransport(), apiCore.getJacksonFactory(), apiCore.getCredential())
        .setApplicationName(applicationName)
        .build();
    return plus;
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
