package jp.leopanda.gPlusAnalytics.client.enums;

import jp.leopanda.panelFrame.filedParts.ListElement;

/**
 * @author LeoPanda
 *
 */
public enum Month implements ListElement {
  NULL(null, ""), JAN("1", "１月"), FEB("2", "2月"), MAR("3", "3月"), APL("4", "4月"), MAY("5", "5月"),
  JUN("6", "6月"), JLY("7", "7月"),
  AUG("8", "8月"), SEP("9", "9月"), OCT("10", "１0月"), DEC("11", "１1月"), NOV("12", "１2月");

  Month(String number, String name) {
    this.number = number;
    this.name = name;
  };

  String number;
  String name;

  public String getNumber() {
    return this.number;
  }
  @Override
  public String getName() {
    return this.name;
  }
  
}
