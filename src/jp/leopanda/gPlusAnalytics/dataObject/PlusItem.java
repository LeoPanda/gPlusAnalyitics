package jp.leopanda.gPlusAnalytics.dataObject;

/**
 * Google+ REST API アイテムデータセットの同定用
 * PlusItemListのメンバであることを保証するために使用
 * GWT RPCの戻り値として採用する際にabstractにフィールドをもたせると
 * クライアント側で認識できなくなるので、共通フィールドがあっても中身は空っぽにしておく必要がある。
 * @author LeoPanda
 *
 */
public abstract class PlusItem {
}
