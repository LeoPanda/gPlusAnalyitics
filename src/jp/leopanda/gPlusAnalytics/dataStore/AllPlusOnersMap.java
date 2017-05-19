package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.util.SortComparator;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * +1ersアイテムオブジェクトのハンドリングを行う
 * 
 * @author LeoPanda
 *
 */
public class AllPlusOnersMap {
  //PlusOnerID,アイテムオブジェクトの+1ersメモリーマップ
  Map<String, PlusPeople> plusOnersMap = new HashMap<String, PlusPeople>();
  //PlusOnerID,+1数の+1ersメモリーマップ
  Map<String, Integer> numOfPlusOneMap = new HashMap<String, Integer>();

  /**
   * PlusOner毎の+1数マップを返す
   * @return
   */
  public Map<String, Integer> getNumOfPlusOneMap() {
    return numOfPlusOneMap;
  }

  /**
   * 全+1ersをメモリーマップに集約する
   * @param plusOners 集計前の+1ersアイテムオブジェクトリスト
   */
  public void addToMap(List<PlusPeople> plusOners) {
    for (PlusPeople plusOner : plusOners) {
      String plusOneId = plusOner.getId();
      if (plusOnersMap.get(plusOneId) == null) {
        plusOnersMap.put(plusOneId, plusOner);
        numOfPlusOneMap.put(plusOneId, 1);
      } else {
        int numOfPlusOne = numOfPlusOneMap.get(plusOneId);
        numOfPlusOneMap.put(plusOneId, numOfPlusOne + 1);
      }
    }
  }

  /**
   * ID毎に+1数を集計された+1ersのアイテムオブジェクトリストを取得する
   * 
   * @return +1ersのアイテムオブジェクトリスト
   */
  public List<PlusPeople> getPlusOners() {
    List<PlusPeople> plusOners = new ArrayList<PlusPeople>();
    for (PlusPeople plusOner : plusOnersMap.values()) {
      plusOner.setNumOfPlusOne(numOfPlusOneMap.get(plusOner.getId()));
      plusOners.add(plusOner);
    }
    // +1ersを+1数降順にソート
    Collections.sort(plusOners, new SortComparator().getPlusOnerDecendingOrder());
    return plusOners;
  }

}
