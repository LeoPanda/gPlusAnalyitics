package jp.leopanda.gPlusAnalytics.dataObject;

import jp.leopanda.gPlusAnalytics.interFace.IsFilterable;

/**
 * Google+ REST API アイテムデータセットの同定用 PlusItemListのメンバであることを保証するために使用 GWT
 * RPCの戻り値として子クラスを使用する場合、親のabstractにフィールドをもたせると クライアント側で認識できなくなるので、共通フィールドがあっても中身は空っぽにしておく必要がある。
 * 
 * @author LeoPanda
 *
 */
public abstract class PlusItem implements IsFilterable {
}
