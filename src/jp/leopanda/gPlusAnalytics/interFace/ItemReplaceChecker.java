package jp.leopanda.gPlusAnalytics.interFace;

/**
 * アイテム更新判定用関数インターフェース
 * 
 * @author LeoPanda
 *
 */
@FunctionalInterface
public interface ItemReplaceChecker<I> {
  boolean replaceWhen(I newItem, I sourceItem);
}