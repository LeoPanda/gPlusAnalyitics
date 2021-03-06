package jp.leopanda.gPlusAnalytics.client.enums;


/**
 * チャートメニューに載せるチャートの種類
 * 
 * @author LeoPanda
 *
 */
public enum ChartInfo {
  ACTIVIY_COLUMN("アクテビティ別ユーザー頻度分布", 2, 1000, 600), ACTIVITY_CALENDAR("日付別＋１数分布", 2, 1400, 700),
  NUM_OF_PLUSONE("ユーザー頻度別＋１数", 1, 700, 700), PLUSONRES_PIE("＋１ユーザー頻度分布", 1, 700, 700),
  // GENDER_PIE("＋１ユーザー性別分布", 1, 700, 700),
  // LANGUAGE_PIE("+1ユーザー言語分布",1,700,700),
  POSTCIRCLE_PIE("投稿先分布", 2, 700, 700);
  public String title; // グラフのタイトル
  public int occupiedColum; // グラフパネル上の専有カラム数
  public int width;
  public int height;

  ChartInfo(String title, int occupiedColum, int width, int height) {
    this.title = title;
    this.occupiedColum = occupiedColum;
    this.width = width;
    this.height = height;
  }
}
