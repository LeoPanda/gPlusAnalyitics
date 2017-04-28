package jp.leopanda.gPlusAnalytics.client;

/**
 * 単位文字列
 * @author LeoPanda
 *
 */
public class Statics {
  public final static String lengthUnit = "px";
  
  public static String getLengthWithUnit(int length){
    return String.valueOf(length) + lengthUnit;
  }

}
