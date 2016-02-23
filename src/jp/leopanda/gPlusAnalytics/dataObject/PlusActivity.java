package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class PlusActivity extends PlusItem implements Serializable {
	/**
	 * 
	 */
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
	
	private List<String> plusOnerIds;
	private int firstLookers = 0;
	private int lowMiddleLookers = 0;
	private int highMiddleLookers = 0;
	private int highLookers = 0;
	
	
	/**
	 * 
	 */
	public PlusActivity() {
		this.object = new ItemObject();
		this.access = new ItemAccess();
	}
	
	/*
	 * getter
	 */
	public String getActorId(){
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
	public List<String> getAttachmentImageUrls() {
		return this.object.getAttachmentImageUrls();
	}
	public Integer getNumOfPlusOners() {
		return this.object.getPlusoners().getTotalItems();
	}
	public String getAccessDescription() {
		return this.access.getDescription();
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
	public List<String> getPlusOnerIds(){
		return plusOnerIds;
	}

/*
 * setter
 */
	public void setTitle(String title){
		this.title = title;
	}
	public void setId(String id){
		this.id = id;
	}
	public void setActorId(String actorId){
		if(this.actor == null){
			this.actor = new Actor();			
		}
		this.actor.id = actorId;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public void setPublished(Date published){
		this.published = published;
	}
	public void setUpdated(Date updated){
		this.updated = updated;
	}
	public void setItemObjectContent(String content){
		if(this.object == null){
			this.object = new ItemObject();			
		}
		this.object.content = content;
	}
	public void setAttacimentImageUrls(List<String> urls){
		if(this.object == null){
			this.object = new ItemObject();			
		}
		this.object.setAttachmentImageUrls(urls);
	}
	public void setNumOfPlusOners(int num){
		if(this.object == null){
			this.object = new ItemObject();			
		}
		this.object.plusoners = new Users(); 
		this.object.plusoners.totalItems = num;
	}
	public void setAccessDescription(String description){
		if(this.access == null){
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
	public void setPlusOnerIds(List<String> plusOnerIds){
		this.plusOnerIds = plusOnerIds;
	}
}
