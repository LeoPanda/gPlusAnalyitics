package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ClickablePlusItemTable;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ImageColumn;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * Google+ アクテビティ一覧表示テーブル(小型表示)
 * 
 * @author LeoPanda
 *
 */
public class ActivityMiniTablePanel extends ClickablePlusItemTable<PlusActivity> {
  /**
   * コンストラクタ
   */
  public ActivityMiniTablePanel(List<PlusActivity> items) {
    super(items);
  }

  TextColumn<PlusActivity> titleColumn; // タイトル
  ImageColumn<PlusActivity> imageColumn; // 投稿写真
  TextColumn<PlusActivity> numOfPlusOneColumn; // +1er数


  /**
   * 表示カラムをセットする
   */
  @Override
  protected void setColumns() {
    // タイトル
    titleColumn = new TextColumn<PlusActivity>() {
      @Override
      public String getValue(PlusActivity item) {
        return item.getTitle();
      }
    };
    // 投稿写真
    imageColumn = new ImageColumn<PlusActivity>("80",null) {
      @Override
      public SafeHtml getValue(PlusActivity item) {
        return getImageTagFromUrl(item.getFirstAttachmentImageUrl());
      }
    };
    // +1er数
    numOfPlusOneColumn = new TextColumn<PlusActivity>() {

      @Override
      public String getValue(PlusActivity item) {
        return String.valueOf(item.getNumOfPlusOners());
      }
    };
    // カラム表示リストに登録
    this.columSets.add(newColumnSet("タイトル", titleColumn, 400, null));
    this.columSets.add(newColumnSet("写真", imageColumn, 100, null));
    this.columSets.add(newColumnSet("+1", numOfPlusOneColumn, 30,
        HasHorizontalAlignment.ALIGN_RIGHT));
  }


  /**
   * ソート条件をセットする
   */
  @Override
  protected void setSortHandler() {
    return;
  }


}
