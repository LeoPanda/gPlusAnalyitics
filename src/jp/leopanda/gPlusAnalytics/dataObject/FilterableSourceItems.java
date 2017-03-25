package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.ArrayList;
import java.util.List;


import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogCard;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogPanel;
import jp.leopanda.gPlusAnalytics.client.util.ChildCardsProcesser;
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
   * フィルタログパネルを読み取り順次フィルタリングを実行する
   * 
   * @param logPanel
   */
  public void doFilter(FilterLogPanel logPanel) {
    resetItems();
    new ChildCardsProcesser(logPanel) {
      @Override
      public void cardProcess(FilterLogCard card) {
        if (card.getEnableCheck()) {
          doFilterIndivisual(card);
        }
      }
    }.processAll();
  }

  /**
   * フィルタリングを実行する
   * 
   * @param card
   */
  private void doFilterIndivisual(FilterLogCard card) {
    switch (card.getFilterType()) {
    case ACTIVITIES_PLUSONER:
      currentPlusActivities = filterActivities.byPlusOner(currentPlusActivities, card.getPlusOner());
      syncPlusOners();
      break;
    case ACTIVITIES_KEYWORD:
      currentPlusActivities = filterActivities.byKeyword(currentPlusActivities, card.getKeyword());
      syncPlusOners();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      currentPlusActivities = filterActivities.byAccessDescription(currentPlusActivities,
          card.getKeyword());
      syncPlusOners();
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      currentPlusActivities = filterActivities.byPublishedYear(currentPlusActivities,
          card.getKeyword());
      syncPlusOners();
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      currentPlusActivities = filterActivities.byPublishedMonth(currentPlusActivities,
          card.getKeyword());
      syncPlusOners();
      break;
    case PLUSONER_ACTIVITY:
      currentPlusOners = filterPlusOners.byActivity(currentPlusOners, card.getActivity());
      syncActivities();
      break;
    case PLUSONER_KEYWORD:
      currentPlusOners = filterPlusOners.byKeyword(currentPlusOners, card.getKeyword());
      syncActivities();
      break;
    default:
      break;
    }
  }

  /**
   * フィルタしたアクティビティで+1ersを同期する
   */
  private void syncPlusOners() {
    currentPlusOners = filterPlusOners.byActivies(currentPlusOners, currentPlusActivities);
  }

  /**
   * フィルタした+1ersでアクティビティを同期する
   */
  private void syncActivities() {
    currentPlusActivities = filterActivities.byPlusOners(currentPlusActivities, currentPlusOners);
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
   * アイテムリストを初期状態に戻す
   */
  public void resetItems() {
    currentPlusActivities = new TransItemList<PlusActivity>() {
    }.execute(originalPlusActivities);
    currentPlusOners = new TransItemList<PlusPeople>() {
    }.execute(originalPlusOners);
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
