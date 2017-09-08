package jp.leopanda.gPlusAnalytics.server;

import com.google.appengine.api.utils.SystemProperty;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.DeadlineExceededException;

/**
 * 処理実行時間の超過を監視する
 * 
 * @author LeoPanda
 *
 */
public class DeadLineChecker {

  private final long marginMillsec = 10 * 1000;
  private boolean isProductionServer = (SystemProperty.environment
      .value() == SystemProperty.Environment.Value.Production);

  /**
   * 超過時間が迫った場合に例外をスローする
   * 
   * @return
   * @throws DeadlineExceededException
   */
  public boolean checkRuntimeExceed() throws DeadlineExceededException {
    if (!isProductionServer) {
      return false;
    }
    long remainTime = ApiProxy.getCurrentEnvironment().getRemainingMillis();
    boolean almostEcceed = (remainTime - marginMillsec) < 0;
    if (almostEcceed) {
      throw new DeadlineExceededException();
    }
    return almostEcceed;
  }

}
