package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * 日付フォーマット
 * 
 * @author LeoPanda
 *
 */
public enum DateFormat {
  YYMD("yyyy/M/d"), YYMDHM("yy/M/d h:m"), YYMD_JP("yyyy'年'M'月'd'日'"), YYMDHM_JP(
      "yyyy'年'M'月'd'日 'h'時'm'分'"), YYM_JP("yyyy'年'M'月'"),MONTH("M"),YEAR("yyyy");

  private String format;

  private DateFormat(String format) {
    this.format = format;
  }

  public String getValue() {
    return this.format;
  }

}
