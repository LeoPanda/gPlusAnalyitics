package jp.leopanda.gPlusAnalytics.interFace;

/**
 * 実行時間超過が発生する前に通知し、割り込み処理を行うために発生させる例外
 * @author LeoPanda
 *
 */
public class CloseToDeadLineException extends Exception {
  private static final long serialVersionUID = 1L;
  public CloseToDeadLineException(){
    super();
  }
  public CloseToDeadLineException(Throwable e){
    super(e);
  }
}
