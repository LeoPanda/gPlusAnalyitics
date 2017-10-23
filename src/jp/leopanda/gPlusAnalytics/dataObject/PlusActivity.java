package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Google+ API activity Item object
 * 
 * @author LeoPanda
 *
 */
public class PlusActivity extends PlusItem implements Serializable {
  private static final long serialVersionUID = 1L;
  public String kind;
  public String title;
  public String id;
  public String url;

  public Actor actor;
  public Date published;
  public Date updated;
  public ItemObject object;
  public ItemAccess access;

  public List<String> plusOnerIds;
  public int firstLookers = 0;
  public int lowMiddleLookers = 0;
  public int highMiddleLookers = 0;
  public int highLookers = 0;

  /**
   * コンストラクタ
   */
  public PlusActivity() {
    this.object = new ItemObject();
    this.access = new ItemAccess();
  }

  private Optional<Actor> getActor() {
    return Optional.ofNullable(actor);
  }

  private Optional<ItemAccess> getAccess() {
    return Optional.ofNullable(access);
  }

  public String getActorId() {
    return getActor().orElse(new Actor()).id;
  }

  public String getKind() {
    return kind;
  }

  public String getTitle() {
    return title;
  }

  public String getId() {
    return id;
  }

  @Override
  public String getUrl() {
    return url;
  }

  public Date getPublished() {
    return published;
  }

  public Date getUpdated() {
    return updated;
  }

  private Optional<ItemObject> getObject() {
    return Optional.ofNullable(object);
  }

  public Optional<String> getItemOjbectContent() {
    if (getObject().isPresent()) {
      return Optional.ofNullable(object.getContent());
    }
    return Optional.ofNullable(null);
  }

  public Integer getNumOfPlusOners() {
    if (getObject().isPresent()) {
      return this.object.getPlusoners().getTotalItems();
    }
    return null;
  }

  public Optional<String> getFirstAttachmentImageUrl() {
    if (getObject().isPresent()) {
      return this.object.getFirstAttachmentImagetUrl();
    }
    return Optional.ofNullable(null);
  }

  public Optional<Long> getFirstAttachmentImageHeigt() {
    if (getObject().isPresent()) {
      return this.object.getFirstAttachmentImageHeight();
    }
    return Optional.ofNullable(null);
  }

  public Optional<Long> getFirstAttachmentImageWidth() {
    if (getObject().isPresent()) {
      return this.object.getFirstAttachmentImageWidth();
    }
    return Optional.ofNullable(null);
  }

  /**
   * @return accessDescription
   */
  public String getAccessDescription() {
    String result = this.access.getDescription();
    if (result.equals("Public") || result.equals("Collection")) {
      result = "日本の絶景";
    }
    return result;
  }

  public int getFirstLookers() {
    return firstLookers;
  }

  public int getLowMiddleLookers() {
    return lowMiddleLookers;
  }

  public int getHighMiddleLookers() {
    return highMiddleLookers;
  }

  public int getHighLookers() {
    return highLookers;
  }

  public List<String> getPlusOnerIds() {
    return plusOnerIds;
  }

  /*
   * setter
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * set actorId
   * 
   * @param actorId actor ID
   */
  public void setActorId(String actorId) {
    getActor().orElse(new Actor()).id = actorId;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setPublished(Date published) {
    this.published = published;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  /**
   * set ItemObjectContent
   * 
   * @param content content
   */
  public void setItemObjectContent(String content) {
    object = getObject().orElse(new ItemObject());
    object.content = content;
  }

  /**
   * set Attachment Image Urls
   * 
   * @param urls attachment image Urls
   */
  public void setAttacimentImageUrls(List<String> urls) {
    object = getObject().orElse(new ItemObject());
    object.setAttachmentImageUrls(urls);
  }

  /**
   * set Number of PlusOners
   * 
   * @param num number of plusoners
   */
  public void setNumOfPlusOners(int num) {
    object = getObject().orElse(new ItemObject());
    object.plusoners = new Users();
    object.plusoners.totalItems = num;
  }

  /**
   * set access description
   * 
   * @param description access description
   */
  public void setAccessDescription(String description) {
    access = getAccess().orElse(new ItemAccess());
    access.description = description;
  }

  public void setFirstLookers(int firstLookers) {
    this.firstLookers = firstLookers;
  }

  public void setLowMiddleLookers(int lowMiddleLookers) {
    this.lowMiddleLookers = lowMiddleLookers;
  }

  public void setHighMiddleLookers(int highMiddleLookers) {
    this.highMiddleLookers = highMiddleLookers;
  }

  public void setHighLookers(int highLookers) {
    this.highLookers = highLookers;
  }

  public void setPlusOnerIds(List<String> plusOnerIds) {
    this.plusOnerIds = plusOnerIds;
  }

}
