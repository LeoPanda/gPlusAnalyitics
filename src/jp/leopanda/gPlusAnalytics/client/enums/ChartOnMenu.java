package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * チャートメニューに載せるチャートの種類
 * 
 * @author LeoPanda
 *
 */
public enum ChartOnMenu {
	ACTIVIY_COLUMN("アクテビティ別ユーザー頻度分布", 2, 1000, 600),
	ACTIVITY_CALENDAR("日付別＋１数分布", 2, 1200, 700),
	NUM_OF_PLUSONE("ユーザー頻度別＋１数", 1, 700, 700), 
	PLUSONRES_PIE("＋１ユーザー頻度分布", 1, 700, 700);
	public String name; // グラフのタイトル
	public int occupiedColum; // グラフパネル上の専有カラム数
	public int width;
	public int height;
	ChartOnMenu(String name, int occupiedColum, int width, int height) {
		this.name = name;
		this.occupiedColum = occupiedColum;
		this.width = width;
		this.height = height;
	}
}