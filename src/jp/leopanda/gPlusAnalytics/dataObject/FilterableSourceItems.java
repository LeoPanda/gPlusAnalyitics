package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.util.FilterActivities;
import jp.leopanda.gPlusAnalytics.client.util.FilterPlusOners;

/**
 * ソースデータアイテムオブジェクトの記憶域とフィルター機能を提供する
 * 
 * @author LeoPanda
 *
 */
public class FilterableSourceItems {
  // カレントアイテム
  private List<PlusActivity> currentPlusActivities;
  private List<PlusPeople> currentPlusOners;
  // オリジナル状態の記憶域
  private List<PlusActivity> originalPlusActivities;
  private List<PlusPeople> originalPlusOners;
  // フィルタメソッドオブジェクト
  FilterActivities filterActivities = new FilterActivities();
  FilterPlusOners filterPlusOners = new FilterPlusOners();
  // フィルターログ
  private String filterLog = "";
  
  /**
   * ログの種類
   */
  private enum LogType {
    ACTIVITY("アクテビティ"), PLUSONER("+1er");
    LogType(String word) {
      this.word = word;
    }

    public String word;
  }

  /**
   * コンストラクタ
   * 
   * @param plusActivities
   * @param plusOners
   */
  public FilterableSourceItems(List<PlusActivity> plusActivities, List<PlusPeople> plusOners) {
    this.currentPlusActivities = plusActivities;
    this.currentPlusOners = plusOners;
    this.originalPlusActivities = new TransItemList<PlusActivity>() {
    }.execute(plusActivities);
    this.originalPlusOners = new TransItemList<PlusPeople>() {
    }.execute(plusOners);
  }

  /**
   * カレントのアクテビティリストを取得する
   * 
   * @return
   */
  public List<PlusActivity> getActivities() {
    return this.currentPlusActivities;
  }

  /**
   * カレントの+1ersリストを取得する
   * 
   * @return
   */
  public List<PlusPeople> getPlusOners() {
    return this.currentPlusOners;
  }

  /**
   * タイプを指定してカレントアイテムリストを取得する
   * 
   * @param itemType
   *          取得したいアイテムリストのタイプ
   * @return
   */
  public PlusItemList<?> getItemList(ItemType itemType) {
    PlusItemList<?> itemList = null;
    if (itemType == ItemType.ACTIVITIES) {
      PlusActivityList activityList = new PlusActivityList();
      activityList.setItems(this.currentPlusActivities);
      itemList = activityList;
    } else if (itemType == ItemType.PLUSONERS) {
      PlusPeopleList plusPeopleList = new PlusPeopleList();
      plusPeopleList.setItems(this.currentPlusOners);
      itemList = plusPeopleList;
    }
    return itemList;
  }

  /**
   * +フィルタログを取得する
   * 
   * @return
   */
  public String getFilterLog() {
    return this.filterLog;
  }

  /**
   * アイテムリストを初期状態に戻す
   */
  public void resetItems() {
    currentPlusActivities = new TransItemList<PlusActivity>() {
    }.execute(originalPlusActivities);
    currentPlusOners = new TransItemList<PlusPeople>() {
    }.execute(originalPlusOners);
    filterLog = "";
  }

  /**
   * アクテビティを単一の+1erでフィルタする
   * 
   * @param plusOnerId
   */
  public void filterActiviesByPlusOner(PlusPeople plusPeople) {
    currentPlusActivities = filterActivities.byPlusOner(currentPlusActivities,plusPeople);
    addFilterLog(LogType.ACTIVITY,plusPeople.displayName);
  }

  /**
   * アクテビティをキーワードでフィルタする
   * 
   * @param keyword
   */
  public void filterActivitiesByKeyword(String keyword) {
    currentPlusActivities = filterActivities.byKeyword(currentPlusActivities,keyword);
    currentPlusOners = filterPlusOners.byActivies(currentPlusOners, currentPlusActivities);
    addFilterLog(LogType.ACTIVITY,keyword);
  }

  /**
   * アクテビティを投稿先カテゴリでフィルタする
   * 
   * @param keyword
   */
  public void filterActivitiesByAccessDescription(String keyword) {
    currentPlusActivities = filterActivities.byAccessDescription(currentPlusActivities,keyword);
    currentPlusOners = filterPlusOners.byActivies(currentPlusOners, currentPlusActivities);
    addFilterLog(LogType.ACTIVITY,keyword);
  }

  /**
   * アクテビティを投稿日でフィルタする
   * 
   * @param comparator
   */
  public void filterActivitesByPublished(String year, String month) {
    currentPlusActivities = filterActivities.byPublished(currentPlusActivities,year, month);
    currentPlusOners = filterPlusOners.byActivies(currentPlusOners, currentPlusActivities);
    String logword = year != null ? year + "年" : "";
    logword += month != null ? month + "月" : "";
    addFilterLog(LogType.ACTIVITY,logword);
  }

  /**
   * +1erを単一のアクテビティでフィルタする
   * 
   * @param activity
   */
  public void filterPlusOnersByActivity(PlusActivity activity) {
    currentPlusOners = filterPlusOners.byActivity(currentPlusOners, activity);
    addFilterLog(LogType.PLUSONER,activity.getTitle().substring(0, 10));
  }

  /**
   * +1erをキーワードでフィルタする
   * 
   * @param keyword
   */
  public void filterPlusOnersByKeyword(String keyword) {
    currentPlusOners = filterPlusOners.byKeyword(currentPlusOners, keyword);
    currentPlusActivities = filterActivities.byPlusOners(currentPlusActivities,currentPlusOners);
    addFilterLog(LogType.PLUSONER,keyword);
  }

  /**
   * フィルターログにワードを追加する
   * 
   * @param logWord
   */
  private void addFilterLog(LogType logType,String logWord) {
    if(logWord.length() > 0){
      filterLog += filterLog.length() > 0 ? "& " : "";
      filterLog += logType.word + "(" + logWord + ")";
    }
  }

  /**
   * アイテムリストを別記憶域に移送する
   * 
   * @author LeoPanda
   *
   * @param <I>
   */
  private abstract class TransItemList<I extends PlusItem> {
    public List<I> execute(List<I> inputItems) {
      List<I> outputItems = new ArrayList<I>();
      for (I i : inputItems) {
        outputItems.add(i);
      }
      return outputItems;
    }
  }
}
