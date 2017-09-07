package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.google.apphosting.api.ApiProxy;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;

/**
 * GAE処理時間超過前にインターセプトして例外処理を発生させる
 * 
 * @author LeoPanda
 *
 */
public abstract class DeadlineInterrupter<I extends PlusItem,S extends SourceItems> {
  // 追加処理実行のための猶予時間
  private final long marginTimeMills = 10 * 1000;
  Logger logger = Logger.getLogger("DeadlineInterrupter");
  List<I> unprocessedItems;
  S sourceItems;
  DeadlineInterrupter(List<I> unprocessedItems,S sourceItems){
    this.unprocessedItems = unprocessedItems;
    this.sourceItems = sourceItems;
  }
  /**
   * 処理時間超過時に実行するタスクを設定する
   */
  public void setInterruptTask(){
    Timer timer = new Timer();
    long delay = getRemainingMills() - marginTimeMills;
    logger.info("set exceed time =" + String.valueOf(delay) + "millisec.");
    timer.schedule(new DeadLine(), delay);
  }

  /**
   * 割り込み時に実行するメソッド
   * @param sourceItems
   * @throws Exception
   */
  public abstract void whenExceeded(List<I> unprocessedItems,S sourceItems) throws Exception;

  class DeadLine extends TimerTask {
    @Override
    public void run() {
      try {
        logger.warning("Process exceeded. Requesting extention.");
        whenExceeded(DeadlineInterrupter.this.unprocessedItems,DeadlineInterrupter.this.sourceItems);
      } catch (Exception e) {
        logger.severe("Error oocured duling requesting extention process.");
        e.printStackTrace();
      }
    }

  }

  /**
   * GAEの処理可能時間を取得する
   * 
   * @return
   */
  private long getRemainingMills() {
    return ApiProxy.getCurrentEnvironment().getRemainingMillis();
  }

}
