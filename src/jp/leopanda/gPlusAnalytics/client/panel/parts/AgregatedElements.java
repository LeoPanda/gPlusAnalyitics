package jp.leopanda.gPlusAnalytics.client.panel.parts;

import jp.leopanda.panelFrame.filedParts.ListElement;

/**
 * 集約値リストの構成要素
 * @author LeoPanda
 *
 */
public class AgregatedElements<V> implements ListElement{
  String elementName;
  V elementValue;
  /**
   * コンストラクタ
   * @param elementName
   * @param elementValue
   */
  public AgregatedElements(String elementName,V elementValue){
    this.elementName = elementName;
    this.elementValue = elementValue;
  }
  /* 
   * リストの表示名を得る
   */
  @Override
  public String getName() {
    return elementName;
  }
  /**
   * 要素の実値を得る
   * @return
   */
  public V getValue(){
    return elementValue;
  }
  
  
}