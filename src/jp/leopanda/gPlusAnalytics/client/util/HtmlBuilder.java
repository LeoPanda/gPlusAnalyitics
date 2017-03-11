package jp.leopanda.gPlusAnalytics.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Common utility for building SafeHtml
 * @author LeoPanda
 *
 */
public class HtmlBuilder {

  private SafeHtmlBuilder builder = new SafeHtmlBuilder();


  /**
   * HTMLの生成
   * @return
   */
  public SafeHtml getSafeHtml() {
    return builder.toSafeHtml();
  }

  /**
   * アクティビティ要約表示用イメージタグの追加
   * @param imgUrl
   */
  public void appendActivityTumbnailImg(String imgUrl) {
    appendImgTag(imgUrl, "80", "80");
  }
  /**
   * 人物要約表示用イメージタグの追加
   * @param imgUrl
   */
  public void appendPeopleTumbnailImg(String imgUrl) {
    appendImgTag(imgUrl, "50", "50");
  }

  /**
   * イメージタグの追加
   * @param imgUrl
   * @param height
   * @param width
   */
  private void appendImgTag(String imgUrl, String height, String width) {
    builder
        .appendHtmlConstant(
            "<img height=\"" + height + "\" width =\"" + width + "\" src=\"")
        .appendEscaped(imgUrl).appendHtmlConstant("\">").appendHtmlConstant("<br/>");
  }

}
