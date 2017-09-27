package jp.leopanda.gPlusAnalytics.interFace;

/**
 * アイテム更新判定用関数インターフェース
 * 
 * @author LeoPanda
 *
 */
@FunctionalInterface
public interface ItemChecker<I> {
  boolean replaceWhen(I newItem, I sourceItem);
}
