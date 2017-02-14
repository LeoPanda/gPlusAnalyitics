package jp.leopanda.gPlusAnalytics.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.plus.model.Activity;

import jp.leopanda.gPlusAnalytics.dataObject.Actor;
import jp.leopanda.gPlusAnalytics.dataObject.Attachment;
import jp.leopanda.gPlusAnalytics.dataObject.ItemAccess;
import jp.leopanda.gPlusAnalytics.dataObject.ItemObject;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.Users;

/**
 * Plus API ActivityからPlusActivityデータを作成する
 * 
 * @author LeoPanda
 *
 */
public class ActivityMaker {

  Logger logger = Logger.getLogger("ActivityMaker");

  /**
   * PlusActivityの生成
   * @param activity
   * @return
   */
  public PlusActivity get(Activity activity) {
    PlusActivity plusActivity = new PlusActivity();
    plusActivity.kind = activity.getKind();
    plusActivity.title = activity.getTitle();
    plusActivity.id = activity.getId();
    plusActivity.url = activity.getUrl();
    plusActivity.actor = activity.getActor() == null ? null : getPlusActor(activity.getActor());
    plusActivity.published = new Date(activity.getUpdated().getValue());
    plusActivity.updated = new Date(activity.getUpdated().getValue());
    plusActivity.object = getItemObject(activity.getObject());
    plusActivity.access = new ItemAccess();
    plusActivity.access.description = activity.getAccess() == null ? null : activity.getAccess().getDescription();
    return plusActivity;

  }

  private Actor getPlusActor(com.google.api.services.plus.model.Activity.Actor actor) {
    Actor plusActor = new Actor();
    plusActor.id = actor.getId();
    plusActor.displayName = actor.getDisplayName();
    plusActor.url = actor.getUrl();
    return plusActor;
  }

  private ItemObject getItemObject(com.google.api.services.plus.model.Activity.PlusObject object) {
    ItemObject itemObject = new ItemObject();
    itemObject.content = object.getContent();
    itemObject.plusoners = object.getPlusoners() == null ? null
        : getplusoners(object.getPlusoners());
    itemObject.attachments = object.getAttachments() == null ? null
        : getAttachments(object.getAttachments());
    return itemObject;
  }

  private Users getplusoners(
      com.google.api.services.plus.model.Activity.PlusObject.Plusoners plusOners) {
    Users users = new Users();
    users.totalItems = new Integer(plusOners.getTotalItems().toString());
    return users;
  }

  private List<Attachment> getAttachments(
      List<com.google.api.services.plus.model.Activity.PlusObject.Attachments> attachments) {
    List<Attachment> itemAttachments = new ArrayList<Attachment>();
    for (com.google.api.services.plus.model.Activity.PlusObject.Attachments attachment : attachments) {
      Attachment itemAttachment = getAttachment(
          attachment.getImage() == null ? null : attachment.getImage().getUrl());
      itemAttachments.add(itemAttachment);
    }
    return itemAttachments;
  }

  private Attachment getAttachment(String url) {
    if(url == null){
      return null;
    }
    Attachment attachment = new Attachment();
    attachment.setImageUrl(url);
    return attachment;
  }

}
