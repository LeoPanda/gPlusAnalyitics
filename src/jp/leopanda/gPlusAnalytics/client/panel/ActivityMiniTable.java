package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.panel.parts.ImageColumn;
import jp.leopanda.gPlusAnalytics.client.panel.parts.PlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.parts.StringColumn;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル(小型表示)
 * 
 * @author LeoPanda
 *
 */
public class ActivityMiniTable extends PlusItemTable<PlusActivity> {
  /**
   * コンストラクタ
   */
  public ActivityMiniTable(List<PlusActivity> items) {
    super(items);
  }

  StringColumn<PlusActivity> titleColumn; // タイトル
  ImageColumn<PlusActivity> imageColumn; // 投稿写真
  StringColumn<PlusActivity> numOfPlusOneColumn; // +1er数

  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    titleColumn = new StringColumn<PlusActivity>(item -> item.getTitle());
    imageColumn = new ImageColumn<PlusActivity>("80", null,
        item -> item.getFirstAttachmentImageUrl());
    numOfPlusOneColumn = new StringColumn<PlusActivity>(
        item -> String.valueOf(item.getNumOfPlusOners()));
    // カラム表示リストに登録
    addColumnSet("タイトル", titleColumn, 400, null);
    addColumnSet("写真", imageColumn, 100, null);
    addColumnSet("+1", numOfPlusOneColumn, 30, HasHorizontalAlignment.ALIGN_RIGHT);
  }

}
