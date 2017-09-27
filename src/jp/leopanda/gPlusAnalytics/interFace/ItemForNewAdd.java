package jp.leopanda.gPlusAnalytics.interFace;

/**
 * アイテムリスト新規追加用関数インターフェース
 * @author LeoPanda
 *
 */
@FunctionalInterface
public interface ItemForNewAdd<I> {
  I setItem(I newItem) throws Exception;
}
