package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * パネルに使用する CSSのスタイル名
 * @author LeoPanda
 *
 */
public enum CssStyle {
	None(""),
	NormalCell("normal"),
	LabelNormal("headerLabel"),
	TitleLabel("titleLabel"),
	CountLabel("countLabel"),
	PageButton("pageButton"),
	FilterButton("filterButton"),
	ResetButton("resetButton");
	
	private String styleName;
	
	private CssStyle(String styleName){
		this.styleName = styleName;
	}
	
	public String getValue(){
		return styleName;
	}
}
