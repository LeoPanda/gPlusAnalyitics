package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

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
public abstract class ImageColumn<I> extends Column<I, SafeHtml> {
  static final SafeHtmlCell cell = new SafeHtmlCell();
  private String height = "";
  private String width = "";
  private String imgUrl = null;

  /**
   * コンストラクタ
   */
  public ImageColumn() {
    super(cell);
  }
  /**
   * イメージの高さを指定する
   * @param height
   */
  protected void setImageHeight(int height){
    this.height = String.valueOf(height);
  }
  /**
   * イメージの幅を指定する
   * @param width
   */
  protected void setImageWidth(int width){
    this.width = String.valueOf(width);
  }
  /**
   * イメージのソースURLをセットする
   * @param url
   */
  protected void setImageUrl(String url){
    this.imgUrl = url;
  }
  /**
   * イメージ表示用のHTMLを取得する
   * @return
   */
  protected SafeHtml getImageTag() {
    SafeHtml imageTag;
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    if(imgUrl == null){
      return null;
    }
    imageTag = builder
        .appendHtmlConstant(
            "<img " +
                (height.length() > 0 ? "height=\"" + height + "\"" : "") +
                (width.length() > 0 ? "width =\"" + width + "\"" : "") +
                " src=\"")
        .appendEscaped(imgUrl).appendHtmlConstant("\">").appendHtmlConstant("<br/>").toSafeHtml();
    clearValiable();
    return imageTag;
  }
  /**
   * 変数を初期化する
   */
  private void clearValiable(){
    height = "";
    width = "";
    imgUrl = "";
  }
  

}
