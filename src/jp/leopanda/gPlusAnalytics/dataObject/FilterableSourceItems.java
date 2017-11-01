package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jp.leopanda.gPlusAnalytics.client.enums.FilterBooleans;
import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogPanel;
import jp.leopanda.gPlusAnalytics.client.panel.parts.FilterLogCard;
import jp.leopanda.gPlusAnalytics.client.util.FilteredActivities;
import jp.leopanda.gPlusAnalytics.client.util.FilteredPlusOners;
import jp.leopanda.gPlusAnalytics.client.util.ItemFilter;

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
    originalPlusActivities = duplicateItems(plusActivities);
    originalPlusOners = duplicateItems(plusOners);
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
      case ACTIVITIES_BY_PLUSONER:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byPlusOner(card.getPlusOner()));
        break;
      case ACTIVITIES_BY_KEYWORD:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byKeyword(card.getKeyword()));
        break;
      case ACTIVITIES_BY_ACCESSDESCRIPTION:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byAccessDescription(card.getKeyword()));
        break;
      case ACTIVITIES_BY_PUBLISHED_YEAR:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byPublishedYear(card.getKeyword()));
        break;
      case ACTIVITIES_BY_PUBLISHED_MONTH:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byPublishedMonth(card.getKeyword()));
        break;
      case ACTIVITIES_BY_NUMOFPLUSONE:
        currentPlusActivities = filterAndCombineAcitivies(card.getBooleansValue(),
            items -> new FilteredActivities(items).byNumOfPlusOne(card.getNumOfPlusOneKeyword()));
        break;
      case PLUSONERS_BY_ACTIVITY:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items).byActivity(card.getActivity()));
        break;
      case PLUSONERS_BY_KEYWORD:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items).byKeyword(card.getKeyword()));
        break;
      case PLUSONERS_BY_NUMOFPLUSONE:
        currentPlusOners = filterAndCombinePlusOners(card.getBooleansValue(),
            items -> new FilteredPlusOners(items).byNumOfPlusOne(card.getNumOfPlusOneKeyword()));
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
    currentPlusOners = new FilteredPlusOners(currentPlusOners).byActivies(combinedItems);
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
    currentPlusActivities =
        new FilteredActivities(currentPlusActivities).byPlusOners(combinedItems);
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
        combinedItems = new ItemFilter<I>(currentItems).combineOr(function.apply(originalItems));
      default:
        break;
    }
    return combinedItems;
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
    currentPlusActivities = duplicateItems(originalPlusActivities);
    currentPlusOners = duplicateItems(originalPlusOners);
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

  /**
   * ソースアイテムリストを複製コピーする
   * 
   * @param sourceItems
   * @return
   */
  private <I extends PlusItem> List<I> duplicateItems(List<I> sourceItems) {
    return sourceItems.stream().collect(Collectors.toList());
  }
}
