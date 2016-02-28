package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * +1ersアイテムオブジェクトのハンドリングを行う
 * 
 * @author LeoPanda
 *
 */
public class PlusOnerHandler {
  //ID,アイテムオブジェクトの+1ersメモリーマップ
  Map<String, PlusPeople> plusOnersMap = new HashMap<String, PlusPeople>();
  //ID,+1数の+1ersメモリーマップ
  Map<String, Integer> numOfPlusOneMap = new HashMap<String, Integer>();

  public Map<String, Integer> getNumOfPlusOneMap() {
    return numOfPlusOneMap;
  }

  /**
   * アクティビティ毎に散らばる+1ersをメモリーマップ上に集計する
   * @param plusOners 集計前の+1ersアイテムオブジェクトリスト
   */
  public void aggregatePlusOnes(List<PlusPeople> plusOners) {
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
    List<PlusPeople> results = new ArrayList<PlusPeople>();
    for (PlusPeople plusOner : plusOnersMap.values()) {
      plusOner.setNumOfPlusOne(numOfPlusOneMap.get(plusOner.getId()));
      results.add(plusOner);
    }
    return results;
  }

}
