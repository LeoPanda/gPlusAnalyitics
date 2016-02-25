/**
 * 
 */
package jp.leopanda.gPlusAnalytics.interFace;

/**
 * クラスアイテムをフィルター表示可能にする
 * @author LeoPanda
 *
 */
public interface IsFilterable {
	/**
	 * フィルターしたいフィールドの値を取得
	 * @return
	 */
	String getFilterSourceValue();

}
