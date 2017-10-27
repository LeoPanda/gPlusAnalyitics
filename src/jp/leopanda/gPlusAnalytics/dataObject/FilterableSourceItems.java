package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import jp.leopanda.gPlusAnalytics.client.enums.FilterBooleans;
import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogPanel;
import jp.leopanda.gPlusAnalytics.client.panel.parts.FilterLogCard;
import jp.leopanda.gPlusAnalytics.client.util.FilteredActivities;
import jp.leopanda.gPlusAnalytics.client.util.FilteredPlusOners;
import jp.leopanda.gPlusAnalytics.client.util.ItemFilter;
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

  /**
   * コンストラクタ
   * 
   * @param plusActivities
   * @param plusOners
   */
  public FilterableSourceItems(List<PlusActivity> plusActivities, List<PlusPeople> plusOners) {
    this.currentPlusActivities = plusActivities;
    this.currentPlusOners = plusOners;
    this.originalPlusActivities = new TransItemList<PlusActivity>() {}.execute(plusActivities);
    this.originalPlusOners = new TransItemList<PlusPeople>() {}.execute(plusOners);
  }

  /**
   * フィルタログパネルを読み取り順次フィルタリングを実行する
   * 
   * @param logPanel
   */
  public void doFilterEachCards(FilterLogPanel logPanel) {
    resetItems();
    logPanel.forEachCards(card -> doFilter(card));
    sortItems();
  }

  /**
   * カード１枚分のフィルタリングを実行する
   * 
   * @param card
   */
  private void doFilter(FilterLogCard card) {
    switch (card.getFilterType()) {
      case ACTIVITY_TABLE_PLUSONER:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items) {}.byPlusOner(card.getPlusOner()));
        break;
      case ACTIVITIES_KEYWORD:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items) {}.byKeyword(card.getKeyword()));
        break;
      case ACTIVITIES_ACCESSDESCRIPTION:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items) {}.byAccessDescription(card.getKeyword()));
        break;
      case ACTIVITIES_PUBLISHED_YEAR:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items) {}.byPublishedYear(card.getKeyword()));
        break;
      case ACTIVITIES_PUBLISHED_MONTH:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items) {}.byPublishedMonth(card.getKeyword()));
        break;
      case PLUSONER_TABLE_ACTIVITY:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items) {}.byActivity(card.getActivity()));
        break;
      case PLUSONER_KEYWORD:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items) {}.byKeyword(card.getKeyword()));
        break;
      case PLUSONER_NUMOFPLUSONE:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items) {}.byNumOfPlusOne(card.getNumOfPlusOneKeyword()));
        break;
      default:
        break;
    }
  }

  /**
   * 指定されたフィルター式を実行しカレントアクテビティリストに論理結合する
   * 
   * @param booleans
   * @param function
   * @return
   */
  private List<PlusActivity> filterAndCombineAcitivies(FilterBooleans booleans,
      Function<List<PlusActivity>, List<PlusActivity>> function) {
    List<PlusActivity> combinedItems = filterAndCombineItems(originalPlusActivities,
        currentPlusActivities, booleans, function);
    currentPlusOners = syncPlusOners(combinedItems);
    return combinedItems;
  }

  /**
   * 指定されたフィルター式を実行しカレント+1erリストに論理結合する
   * 
   * @param booleans
   * @param function
   * @return
   */
  private List<PlusPeople> filterAndCombinePlusOners(FilterBooleans booleans,
      Function<List<PlusPeople>, List<PlusPeople>> function) {
    List<PlusPeople> combinedItems = filterAndCombineItems(originalPlusOners, currentPlusOners,
        booleans, function);
    currentPlusActivities = syncActivities(combinedItems);
    return combinedItems;
  }

  /**
   * 指定されたフィルター式を実行しカレントアイテムリストに論理結合する
   * 
   * @param currentItems カレントのソースアイテム
   * @param origianlItems オリジナルのソースアイテム
   * @param booleans 論理結合演算子
   * @param function フィルター式
   * @return
   */
  private <I extends PlusItem> List<I> filterAndCombineItems(List<I> originalItems,
      List<I> currentItems,
      FilterBooleans booleans, Function<List<I>, List<I>> function) {
    List<I> combinedItems = new ArrayList<I>();
    switch (booleans) {
      case AND:
        combinedItems = function.apply(currentItems);
        break;
      case OR:
        combinedItems = new ItemFilter<I>(currentItems) {}.combineOr(function.apply(originalItems));
      default:
        break;
    }
    return combinedItems;
  }

  /**
   * フィルタしたアクティビティで+1ersを同期する
   */
  private List<PlusPeople> syncPlusOners(List<PlusActivity> activities) {
    return new FilteredPlusOners(currentPlusOners) {}.byActivies(activities);
  }

  /**
   * フィルタした+1ersでアクティビティを同期する
   */
  private List<PlusActivity> syncActivities(List<PlusPeople> plusOners) {
    return new FilteredActivities(currentPlusActivities) {}.byPlusOners(plusOners);
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
   * @param itemType 取得したいアイテムリストのタイプ
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
    currentPlusActivities = new TransItemList<PlusActivity>() {}.execute(originalPlusActivities);
    currentPlusOners = new TransItemList<PlusPeople>() {}.execute(originalPlusOners);
    sortItems();
  }

  /**
   * アイテムリストをソートする
   */
  private void sortItems() {
    Collections.sort(currentPlusActivities,
        Comparator.comparing(PlusActivity::getPublished).reversed());
    Collections.sort(currentPlusOners,
        Comparator.comparing(PlusPeople::getNumOfPlusOne).reversed());
  }

}
