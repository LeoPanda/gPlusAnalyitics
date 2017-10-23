package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.Optional;

public class PlusPeople extends PlusItem implements Serializable {
  private static final long serialVersionUID = 1L;
  public String id;
  public String url;

  public int numOfPlusOne = 0;

  public String displayName;
  public AttachmentImage image;

  /*
   * getter
   */
  public String getId() {
    return id;
  }

  @Override
  public String getUrl() {
    return url;
  }

  public int getNumOfPlusOne() {
    return numOfPlusOne;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Optional<String> getImageUrl() {
    if (getImage().isPresent()) {
      return image.getUrl();
    }
    return Optional.ofNullable(null);
  }

  private Optional<AttachmentImage> getImage() {
    return Optional.ofNullable(image);
  }

  /*
   * setter
   */
  public void setId(String id) {
    this.id = id;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setNumOfPlusOne(int plusOne) {
    this.numOfPlusOne = plusOne;
  }

  public void setImageUlr(String url) {
    getImage().orElse(new AttachmentImage()).url = url;
  }

}
