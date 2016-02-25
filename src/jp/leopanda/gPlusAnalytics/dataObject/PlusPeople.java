package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;


public class PlusPeople extends PlusItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String id;
	public String url;
	public String gender;
	public String language;
	
	private int numOfPlusOne = 0;

	public String displayName;
	public AttachmentImage image;

	/*
	 * getter
	 */
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
	public String getGender() {
		return gender;
	}
	public String getLanguage() {
		return language;
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
	public void setImageUlr(String url){
		this.image = new AttachmentImage();
		this.image.url = url;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	/* 
	 * @see jp.leopanda.gPlusAnalytics.interFace.IsFilterable#getSourceFieldValue()
	 */
	@Override
	public String getFilterSourceValue() {
		return getDisplayName();
	}

}
