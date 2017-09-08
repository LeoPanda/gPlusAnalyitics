package jp.leopanda.gPlusAnalytics.server;

/**
 * 実行時間超過が発生する前に通知し、割り込み処理を行うために発生させる例外
 * @author LeoPanda
 *
 */
public class CloseToDeadLine extends Exception {
  private static final long serialVersionUID = 1L;
  public CloseToDeadLine(){
    super();
  }
  public CloseToDeadLine(Throwable e){
    super(e);
  }
}
