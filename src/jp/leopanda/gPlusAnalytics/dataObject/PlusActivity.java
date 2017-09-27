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
public class PlusActivity extends PlusItem implements Serializable{
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

  public String getActorId() {
    return actor.id;
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

  public String getItemOjbectContent() {
    return this.object.getContent();
  }

  public Optional<List<String>> getAttachmentImageUrls() {
    return Optional.ofNullable(this.object.getAttachmentImageUrls());
  }

  public Integer getNumOfPlusOners() {
    return this.object.getPlusoners().getTotalItems();
  }

  /**
   * @return accessDescription
   */
  public String getAccessDescription() {
    String result = this.access.getDescription();
    if (result.equals("Public")||result.equals("Collection")) {
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
    if (this.actor == null) {
      this.actor = new Actor();
    }
    this.actor.id = actorId;
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
    if (this.object == null) {
      this.object = new ItemObject();
    }
    this.object.content = content;
  }

  /**
   * set Attachment Image Urls
   * 
   * @param urls attachment image Urls
   */
  public void setAttacimentImageUrls(List<String> urls) {
    if (this.object == null) {
      this.object = new ItemObject();
    }
    this.object.setAttachmentImageUrls(urls);
  }

  /**
   * set Number of PlusOners
   * 
   * @param num number of plusoners
   */
  public void setNumOfPlusOners(int num) {
    if (this.object == null) {
      this.object = new ItemObject();
    }
    this.object.plusoners = new Users();
    this.object.plusoners.totalItems = num;
  }

  /**
   * set access description
   * 
   * @param description access description
   */
  public void setAccessDescription(String description) {
    if (this.access == null) {
      this.access = new ItemAccess();
    }
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
