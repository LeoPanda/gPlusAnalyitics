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
  public Long getImageHeight(){
    return image.getHeight();
  }
  public Long getImageWidth(){
    return image.getWidth();
  }

  public void setImageUrl(String url) {
    image.url = url;
  }
  public void setImageHeight(Long height){
    image.height = height;
  }
  public void setImageWidth(Long width){
    image.width = width;
  }
  

}
