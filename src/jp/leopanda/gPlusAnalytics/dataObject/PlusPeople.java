package jp.leopanda.gPlusAnalytics.dataObject;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlusPeople extends PlusItem implements IsSerializable {
	public String kind;
	public String title;
	public String id;
	public String url;
	private int numOfPlusOne = 0;

	public String displayName;
	public AttachmentImage image;

	/*
	 * getter
	 */
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
	public int getNumOfPlusOne() {
		return this.numOfPlusOne;
	}

	public String getDisplayName() {
		return displayName;
	}
	public String getImageUrl() {
		return this.image.getUrl();
	}
	/*
	 * setter
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}
	public void setTitle(String title) {
		this.title = title;
	}
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
	public void setImageUlr(String url){
		this.image = new AttachmentImage();
		this.image.url = url;
	}
}
