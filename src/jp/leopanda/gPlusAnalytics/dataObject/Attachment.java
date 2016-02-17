package jp.leopanda.gPlusAnalytics.dataObject;


import com.google.gwt.user.client.rpc.IsSerializable;

public class Attachment implements IsSerializable {
	public AttachmentImage image;
	public Attachment() {
		this.image = new AttachmentImage();
	}
    public String getImageUrl(){
    	return image.getUrl();
    }
    
    public void setImageUrl(String url){
    	this.image = new AttachmentImage();
    	image.url = url;
    }

}
