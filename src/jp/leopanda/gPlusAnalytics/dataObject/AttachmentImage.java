package jp.leopanda.gPlusAnalytics.dataObject;


import java.io.Serializable;

public class AttachmentImage implements Serializable {
  private static final long serialVersionUID = 1L;
  public String url;
  public Long height;
  public Long width;

  public String getUrl() {
    return url;
  }
  public Long getHeight(){
    return this.height;
  }
  public Long getWidth(){
    return this.width;
  }

}
