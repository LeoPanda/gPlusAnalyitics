package jp.leopanda.gPlusAnalytics.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Common utility for building SafeHtml
 * 
 * @author LeoPanda
 *
 */
public class HtmlBuilder {

  private SafeHtmlBuilder builder;
  
  public HtmlBuilder(){
    this.builder = new SafeHtmlBuilder();
  }

  /**
   * HTMLの生成
   * 
   * @return
   */
  public SafeHtml toSafeHtml() {
    return builder.toSafeHtml();
  }

  /**
   * アクティビティ要約表示用イメージタグの追加
   * 
   * @param imgUrl
   */
  public SafeHtmlBuilder appendActivityTumbnailImg(String imgUrl) {
    return appendImgTag(imgUrl, "80", "");
  }

  /**
   * 写真グリッド用イメージタグの追加
   * @param imgUrl
   * @return
   */
  public SafeHtmlBuilder appendPhotoGridImg(String imgUrl){
    return appendImgTag(imgUrl,"120","");
  }
  
  /**
   * 人物要約表示用イメージタグの追加
   * 
   * @param imgUrl
   */
  public SafeHtmlBuilder appendPeopleTumbnailImg(String imgUrl) {
    return appendImgTag(imgUrl, "50", "50");
  }

  /**
   * イメージタグの追加
   * 
   * @param imgUrl
   * @param height
   * @param width
   */
  private SafeHtmlBuilder appendImgTag(String imgUrl, String height, String width) {
    return builder
        .appendHtmlConstant(
            "<img " +
                (height.length() > 0 ? "height=\"" + height + "\"" : "") +
                (width.length() > 0 ? "width =\"" + width + "\"" : "") +
                " src=\"")
        .appendEscaped(imgUrl).appendHtmlConstant("\">").appendHtmlConstant("<br/>");
  }

}
