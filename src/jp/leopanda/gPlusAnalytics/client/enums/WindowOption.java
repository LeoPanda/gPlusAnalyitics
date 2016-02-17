package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * window.openのオプションパラメータ
 * @author LeoPanda
 *
 */
public enum WindowOption {
	ItemDetail("width=800,height=800,top=100,left=800,location=0");
	
	private String value;
	private WindowOption(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}

}
