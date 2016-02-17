/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * 日付フォーマット
 * @author LeoPanda
 *
 */
public enum DateFormat {
YYMMDD("yyyy/M/d"),
YYMMDDTime("yy/M/d h:m"),
YYMMDDJp("yyyy'年'M'月'd'日'"),
YYMMDDjPTime("yyyy'年'M'月'd'日 'h'時'm'分'"),
YYMMJp("yyyy'年'M'月'");

private String format;
private DateFormat(String format){
	this.format = format;
}
public String getValue(){
	return this.format;
}

}
