package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.Optional;
import java.util.function.Function;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

/**
 * イメージを表示するテーブルカラム
 * 
 * @author LeoPanda
 *
 */
public class ImageColumn<I> extends Column<I, SafeHtml> {
  static final SafeHtmlCell cell = new SafeHtmlCell();
  private Optional<String> height;
  private Optional<String> width;

  private Function<I, Optional<String>> itemSetter;
  
  /**
   * コンストラクタ
   * @param height
   * @param width
   */
  public ImageColumn(String height, String width,Function<I,Optional<String>> itemSetter) {
    super(cell);
    this.height = Optional.ofNullable(height);
    this.width = Optional.ofNullable(width);
    this.itemSetter = itemSetter;
  }
  
  /* 
   * 表示値の設定
   */
  @Override
  public SafeHtml getValue(I object) {
    return getImageTagFromUrl(itemSetter.apply(object));
  }

  /**
   * イメージ表示用HTMLをURL String から得る
   * 
   * @param imgUrl
   * @return
   */
  private SafeHtml getImageTagFromUrl(Optional<String> imgUrl) {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    return (imgUrl.isPresent() ? getImageTag(imgUrl.get(), builder)
        : getBlankTag(builder));
  }

  /**
   * ブランク行のHTMLタグを生成する
   * 
   * @param builder
   * @return
   */
  private SafeHtml getBlankTag(SafeHtmlBuilder builder) {
    return builder.appendHtmlConstant("<br/>").toSafeHtml();
  }

  /**
   * イメージを表示するHTMLタグを生成する
   * 
   * @param imgUrl
   * @param builder
   * @return
   */
  private SafeHtml getImageTag(String imgUrl, SafeHtmlBuilder builder) {
    return builder
        .appendHtmlConstant(appendWidthAttribute(appendHeightAttribute(setTagPref())))
        .appendHtmlConstant(" src=\"").appendEscaped(imgUrl).appendHtmlConstant("\"><br/>").toSafeHtml();
  }

  /**
   * イメージタグの接頭値を設定する
   * 
   * @return
   */
  private String setTagPref() {
    return "<img ";
  }

  /**
   * イメージタグの高さ属性を設定する
   * 
   * @param htmlText
   * @return
   */
  private String appendHeightAttribute(String htmlText) {
    return height.isPresent() ? htmlText.concat(" height=\"" + height.get() + "\""):htmlText;
  }

  /**
   * イメージタグの幅属性を設定する
   * 
   * @param htmlText
   * @return
   */
  private String appendWidthAttribute(String htmlText) {
    return width.isPresent() ? htmlText.concat(" width=\"" + width + "\""):htmlText;
  }


}
