package jp.leopanda.gPlusAnalytics.client.enums;

import jp.leopanda.panelFrame.filedParts.ListElement;

/**
 * 比較演算子
 * 
 * @author LeoPanda
 *
 */
public enum CompOperator implements ListElement {
  EQ("="), LT("<"), GT(">");
  String name;

  CompOperator(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

}
