package jp.leopanda.gPlusAnalytics.client.enums;

import java.util.function.Consumer;

import jp.leopanda.gPlusAnalytics.client.chart.ActivityCalendarChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityColumnChart;
import jp.leopanda.gPlusAnalytics.client.chart.ActivityLineChart;
import jp.leopanda.gPlusAnalytics.client.chart.NumOfPlusOnePieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PlusOnersPieChart;
import jp.leopanda.gPlusAnalytics.client.chart.PostCirclePieChart;
import jp.leopanda.gPlusAnalytics.interFace.Drawable;

/**
 * チャートパネルに表示するチャートの種類
 * 
 * @author LeoPanda
 *
 */
public enum ChartsOnMenu {
  ACTIVIY_COLUMN("アクテビティ毎リピーター分布", 2, 1000, 600, ItemType.ACTIVITIES, new ActivityColumnChart()),
  ACTIVIY_LINE("リピーター比率トレンド", 2, 1000, 600, ItemType.ACTIVITIES, new ActivityLineChart()),
  ACTIVITY_CALENDAR("日付別＋１数分布", 2, 1400, 700, ItemType.ACTIVITIES, new ActivityCalendarChart()),
  NUM_OF_PLUSONE("リピーター別＋１数比率", 1, 700, 700, ItemType.ACTIVITIES, new NumOfPlusOnePieChart()),
  PLUSONRES_PIE("リピーター数比率", 1, 700, 700, ItemType.PLUSONERS, new PlusOnersPieChart()),
  POSTCIRCLE_PIE("投稿先分布", 2, 700, 700, ItemType.ACTIVITIES, new PostCirclePieChart());
  private String title; // グラフのタイトル
  private int requiredColumsNumber; // グラフパネル上の専有カラム数
  private int width;// チャートの横幅
  private int height;// チャートの高さ
  private ItemType itemType;// ソースアイテムのタイプ
  private Drawable<?> chart;// チャートインスタンス

  ChartsOnMenu(String title, int requiredColum, int width, int height, ItemType itemType,
      Drawable<?> chart) {
    this.title = title;
    this.requiredColumsNumber = requiredColum;
    this.width = width;
    this.height = height;
    this.itemType = itemType;
    this.chart = chart;
    this.chart.setOutlineSpec(title, width, height);
  }

  public static void forEach(Consumer<ChartsOnMenu> processer) {
    for (ChartsOnMenu chartOnPanel : ChartsOnMenu.values()) {
      processer.accept(chartOnPanel);
    }
  }

  public String getTitle() {
    return title;
  }

  public int getRequiredColumsNumber() {
    return requiredColumsNumber;
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

  public Drawable<?> getChart() {
    return chart;
  }
}
