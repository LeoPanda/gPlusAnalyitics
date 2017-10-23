package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemObject implements Serializable {
  private static final long serialVersionUID = 1L;
  public String content;
  public Users plusoners;
  public List<Attachment> attachments;

  /**
   * get Content
   * 
   * @return
   */
  public String getContent() {
    return content;
  }

  /**
   * get Users
   * 
   * @return
   */
  public Users getPlusoners() {
    return plusoners;
  }

  /**
   * 最初のAttachmentを取得する
   * 
   * @return
   */
  public Optional<Attachment> getFirstAttachment() {
    if (Optional.ofNullable(attachments).isPresent()) {
      return attachments.stream().filter(attachment -> attachment != null).findFirst();
    }
    return Optional.ofNullable(null);
  }

  /**
   * 最初のAttachmentのImageUrlを取得する
   * 
   * @return
   */
  public Optional<String> getFirstAttachmentImagetUrl() {
    if (getFirstAttachment().isPresent()) {
      return getFirstAttachment().get().getImageUrl();
    }
    return Optional.ofNullable(null);
  }

  /**
   * 最初のAttachmentのImageの高さを取得する
   * 
   * @return
   */
  public Optional<Long> getFirstAttachmentImageHeight() {
    if (getFirstAttachment().isPresent()) {
      return getFirstAttachment().get().getImageHeight();
    }
    return Optional.ofNullable(null);
  }

  /**
   * 最初のAttachmentのImageの幅を取得する
   * 
   * @return
   */
  public Optional<Long> getFirstAttachmentImageWidth() {
    if (getFirstAttachment().isPresent()) {
      return getFirstAttachment().get().getImageWidth();
    }
    return Optional.ofNullable(null);
  }

  /**
   * setter
   * 
   * @param urls
   *          set attachmentImageUrls
   */
  public void setAttachmentImageUrls(List<String> urls) {
    List<Attachment> attachments = new ArrayList<Attachment>();
    urls.forEach(url -> {
      Attachment attachment = new Attachment();
      attachment.setImageUrl(url);
      attachments.add(attachment);
    });
    this.attachments = attachments;
  }
}
