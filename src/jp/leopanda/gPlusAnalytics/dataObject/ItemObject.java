package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ItemObject implements IsSerializable {
	public String content;
	public Users plusoners;
	public List<Attachment> attachments;
	
	/*
	 * getter
	 */
	public String getContent() {
		return content;
	}
	public Users getPlusoners() {
		return plusoners;
	}
	public List<String> getAttachmentImageUrls(){
		List<String> results = new ArrayList<String>();
		for (Attachment attachment : attachments) {
			results.add(attachment.getImageUrl());
		}
		return results;
	}
	/*
	 * setter
	 */
	public void setAttachmentImageUrls(List<String> urls){
		List<Attachment> attachments = new ArrayList<Attachment>();
		for(String url:urls){
			Attachment attachment = new Attachment();
			attachment.setImageUrl(url);
			attachments.add(attachment);
		}
		this.attachments = attachments;
	}
}
