package jp.leopanda.gPlusAnalytics.client;

/**
 * 単位文字列
 * @author LeoPanda
 *
 */
public class Unit {
  private static String lengthUnit = "px";
  
  public static String getStringWithLength(int length){
    return String.valueOf(length) + lengthUnit;
  }

}
