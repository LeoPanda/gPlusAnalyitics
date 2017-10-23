package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.Optional;

public class Attachment implements Serializable {
  private static final long serialVersionUID = 1L;
  public AttachmentImage image;

  public Attachment() {
    this.image = new AttachmentImage();
  }

  public Optional<String> getImageUrl() {
    if (getImage().isPresent()) {
      return image.getUrl();
    }
    return Optional.ofNullable(null);
  }

  public Optional<Long> getImageHeight() {
    if (getImage().isPresent()) {
      return image.getHeight();
    }
    return Optional.ofNullable(null);
  }

  public Optional<Long> getImageWidth() {
    if (getImage().isPresent()) {
      return image.getWidth();
    }
    return Optional.ofNullable(null);
  }

  public void setImageUrl(String url) {
    image = getImage().orElse(new AttachmentImage());
    image.url = url;
  }

  public void setImageHeight(Long height) {
    image = getImage().orElse(new AttachmentImage());
    image.height = height;
  }

  public void setImageWidth(Long width) {
    image = getImage().orElse(new AttachmentImage());
    image.width = width;
  }

  private Optional<AttachmentImage> getImage() {
    return Optional.ofNullable(image);
  }
}
