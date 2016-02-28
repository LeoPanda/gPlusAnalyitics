package jp.leopanda.gPlusAnalytics.dataObject;


import java.io.Serializable;

public class Attachment implements Serializable {
  private static final long serialVersionUID = 1L;
  public AttachmentImage image;

  public Attachment() {
    this.image = new AttachmentImage();
  }

  public String getImageUrl() {
    return image.getUrl();
  }

  public void setImageUrl(String url) {
    this.image = new AttachmentImage();
    image.url = url;
  }

}
