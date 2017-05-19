package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.Collections;
import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogCard;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogPanel;
import jp.leopanda.gPlusAnalytics.client.util.ChildCardsProcesser;
import jp.leopanda.gPlusAnalytics.client.util.FilterActivities;
import jp.leopanda.gPlusAnalytics.client.util.FilterAndCombineItems;
import jp.leopanda.gPlusAnalytics.client.util.FilterPlusOners;
import jp.leopanda.gPlusAnalytics.client.util.SortComparator;
import jp.leopanda.gPlusAnalytics.client.util.TransItemList;

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
   * カード１枚分のフィルタリングを実行する
   * 
   * @param card
   */
  private void doFilterIndivisual(FilterLogCard card) {
    switch (card.getFilterType()) {
    case ACTIVITIES_PLUSONER:
      currentPlusActivities = new FilterAndCombineItems<PlusActivity>() {
        @Override
        public List<PlusActivity> getFilterdItems(List<PlusActivity> items, FilterLogCard card) {
          return filterActivities.byPlusOner(items, card.getPlusOner());
        }
      }.doFilter(originalPlusActivities, currentPlusActivities, card);
      syncPlusOners();
      break;
    case ACTIVITIES_KEYWORD:
      currentPlusActivities = new FilterAndCombineItems<PlusActivity>() {
        @Override
        public List<PlusActivity> getFilterdItems(List<PlusActivity> items, FilterLogCard card) {
          return filterActivities.byKeyword(items, card.getKeyword());
        }
      }.doFilter(originalPlusActivities, currentPlusActivities, card);
      syncPlusOners();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      currentPlusActivities = new FilterAndCombineItems<PlusActivity>() {
        @Override
        public List<PlusActivity> getFilterdItems(List<PlusActivity> items, FilterLogCard card) {
          return filterActivities.byAccessDescription(items, card.getKeyword());
        }
      }.doFilter(originalPlusActivities, currentPlusActivities, card);
      syncPlusOners();
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      currentPlusActivities = new FilterAndCombineItems<PlusActivity>() {
        @Override
        public List<PlusActivity> getFilterdItems(List<PlusActivity> items, FilterLogCard card) {
          return filterActivities.byPublishedYear(items, card.getKeyword());
        }
      }.doFilter(originalPlusActivities, currentPlusActivities, card);
      syncPlusOners();
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      currentPlusActivities = new FilterAndCombineItems<PlusActivity>() {
        @Override
        public List<PlusActivity> getFilterdItems(List<PlusActivity> items, FilterLogCard card) {
          return filterActivities.byPublishedMonth(items, card.getKeyword());
        }
      }.doFilter(originalPlusActivities, currentPlusActivities, card);
      syncPlusOners();
      break;
    case PLUSONER_ACTIVITY:
      currentPlusOners = new FilterAndCombineItems<PlusPeople>() {
        @Override
        public List<PlusPeople> getFilterdItems(List<PlusPeople> items, FilterLogCard card) {
          return filterPlusOners.byActivity(items, card.getActivity());
        }
      }.doFilter(originalPlusOners, currentPlusOners, card);
      syncActivities();
      break;
    case PLUSONER_KEYWORD:
      currentPlusOners = new FilterAndCombineItems<PlusPeople>() {
        @Override
        public List<PlusPeople> getFilterdItems(List<PlusPeople> items, FilterLogCard card) {
          return filterPlusOners.byKeyword(items, card.getKeyword());
        }
      }.doFilter(originalPlusOners, currentPlusOners, card);
      syncActivities();
      break;
    default:
      break;
    }
    Collections.sort(currentPlusActivities, new SortComparator().getLatestActivitesOrder());
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
  @SuppressWarnings("unchecked")
  public <I extends PlusItem> List<I> getItemList(ItemType itemType) {
    List<I> itemList = null;
    if (itemType == ItemType.ACTIVITIES) {
      itemList = (List<I>) currentPlusActivities;
    } else if (itemType == ItemType.PLUSONERS) {
      itemList = (List<I>) currentPlusOners;
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
}
