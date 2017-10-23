package jp.leopanda.gPlusAnalytics.interFace;

/**
 * Auth認証を使用したAPIコールを実装する
 * @author LeoPanda
 *
 */
@FunctionalInterface
public interface TryApiCall<R> {
  R apply() throws Exception;

}
