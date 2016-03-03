package jp.leopanda.gPlusAnalytics.client;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * プロジェクト共通のグローバル変数を保持する
 * 
 * @author LeoPanda
 *
 */
public class Global {
  // Google apps client ID
  private static final String apiClientId = "1075891745451.apps.googleusercontent.com";

  private static String oAuthToken = null; // Google OAuth2 Token
  private static String googleUserId = null; // google+ ユーザーID
  private static final int milliSecondInADay = 24 * 60 * 60 * 1000; // milliseconds in a day.

  private static List<PlusActivity> activityItems;
  private static List<PlusPeople> plusOners;


  /*
   * getter
   */
  public static String getApiClientid() {
    return apiClientId;
  }

  public static String getAuthToken() {
    return oAuthToken;
  }

  public static String getGoogleUserId() {
    return googleUserId;
  }

  public static List<PlusActivity> getActivityItems() {
    return activityItems;
  }

  public static List<PlusPeople> getPlusOners() {
    return plusOners;
  }

  public static long getMillsecondsByDays(int days) {
    int ret = days * milliSecondInADay;
    return ret;
  }

  /*
   * setter
   */
  public static void setAuthToken(String oauthToken) {
    Global.oAuthToken = oauthToken;
  }

  public static void setgoogeUserId(String userId) {
    Global.googleUserId = userId;
  }

  public static void setActivityItems(List<PlusActivity> items) {
    Global.activityItems = items;
  }

  public static void setPlusOners(List<PlusPeople> items) {
    Global.plusOners = items;
  }
}
