package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * チャートパネルに表示するチャートの種類
 * 
 * @author LeoPanda
 *
 */
public enum ChartType {
  // (チャートのタイトル,パネルに表示できるチャートの最大数,チャートの横幅,チャートの縦幅)
  ACTIVIY_COLUMN("アクテビティ別リピーター頻度分布", 2, 1000, 600, ItemType.ACTIVITIES),
  ACTIVITY_CALENDAR("日付別＋１数分布", 2, 1400, 700, ItemType.ACTIVITIES),
  NUM_OF_PLUSONE("リピーター別＋１数分布", 1, 700, 700, ItemType.ACTIVITIES),
  PLUSONRES_PIE("リピーター頻度分布", 1, 700, 700, ItemType.PLUSONERS),
  POSTCIRCLE_PIE("投稿先分布", 2, 700, 700, ItemType.ACTIVITIES);
  public String title; // グラフのタイトル
  public int occupiedColum; // グラフパネル上の専有カラム数
  public int width;
  public int height;
  public ItemType itemType;

  ChartType(String title, int occupiedColum, int width, int height, ItemType itemType) {
    this.title = title;
    this.occupiedColum = occupiedColum;
    this.width = width;
    this.height = height;
    this.itemType = itemType;
  }

  public String getTitle() {
    return title;
  }

  public int getOccupiedColum() {
    return occupiedColum;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public ItemType getItemType() {
    return itemType;
  }

}
