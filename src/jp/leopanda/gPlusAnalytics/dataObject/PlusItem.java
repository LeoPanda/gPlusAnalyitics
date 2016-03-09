package jp.leopanda.gPlusAnalytics.dataObject;

import jp.leopanda.gPlusAnalytics.interFace.IsFilterable;

/**
 * Google+ REST API アイテムデータセットの同定用 PlusItemListのメンバであることを保証するために使用 GWT
 * RPCの戻り値として子クラスを使用する場合、親のabstractにフィールドをもたせるとクライアント側で認識できなくなるので、共通フィールドがあってもフィールドを記述してはならない。
 * 
 * @author LeoPanda
 *
 */
public abstract class PlusItem implements IsFilterable {
  public abstract String getUrl();
}
