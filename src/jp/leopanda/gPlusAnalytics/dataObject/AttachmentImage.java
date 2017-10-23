package jp.leopanda.gPlusAnalytics.dataObject;


import java.io.Serializable;
import java.util.Optional;

public class AttachmentImage implements Serializable {
  private static final long serialVersionUID = 1L;
  public String url;
  public Long height;
  public Long width;

  public Optional<String> getUrl() {
    return Optional.ofNullable(url);
  }
  public Optional<Long> getHeight(){
    return Optional.ofNullable(height);
  }
  public Optional<Long> getWidth(){
    return Optional.ofNullable(width);
  }

}
