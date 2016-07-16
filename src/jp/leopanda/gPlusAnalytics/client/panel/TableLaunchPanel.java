package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.ItemEventListener;
import jp.leopanda.gPlusAnalytics.interFace.TableEventListener;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 一覧表表示パネル
 * 
 * @author LeoPanda
 *
 */
public class TableLaunchPanel extends HorizontalPanel {
  ActivityTable activityTable;
  PlusOnersTable plusOnersTable;
  FilterableActivityListPanel activityTablePanel;
  FilterablePlusOnerListPanel plusOnersTablePanel;
  TableEventListener eventListener;


  private String activityFilterLog = "";
  private String plusOnerFilterLog = "";

  /**
   * コンストラクタ
   */
  public TableLaunchPanel() {
    if ((Global.getActivityItems() == null) || (Global.getPlusOners() == null)) {
      this.add(new Label("メンテナンスニューからデータストアの初期ロードを行ってください。"));
    } else {
      setUpTables();
      this.add(plusOnersTablePanel);
      this.add(new HTML("<br/>&nbsp;&nbsp;&nbsp;<br/>"));
      this.add(activityTablePanel);
    }
  }

  /**
   * イベントリスナーを設定する
   * 
   * @param eventListener 設定するイベントリスナー
   */
  public void addEventListener(TableEventListener eventListener) {
    this.eventListener = eventListener;
  }

  /**
   * 一覧表に表示されているアクティビティアイテムのリストを取得する
   * 
   * @return 一覧表に表示されているアクティビティアイテムのリスト
   */
  public List<PlusActivity> getActivityDisplayItems() {
    if(activityTable == null){
      return null;
    }
    return activityTable.getDisplayList();
  }

  /**
   * 一覧表に表示されている+1erアイテムのリストを取得する
   * 
   * @return 一覧表に表示されている+1erアイテムのリスト
   */
  public List<PlusPeople> getPlusOnerDisplayItems() {
    if(plusOnersTable == null){
      return null;
    }
    return plusOnersTable.getDisplayList();
  }

  /*
   * 一覧表テーブルと表示パネルを新規作成する
   */
  private void setUpTables() {
    activityTable = new ActivityTable(Global.getActivityItems());
    plusOnersTable = new PlusOnersTable(Global.getPlusOners());
    addTableEventHandler();

    activityTablePanel =
        new FilterableActivityListPanel("アクティビティ一覧", 7, activityTable);
    plusOnersTablePanel =
        new FilterablePlusOnerListPanel("+1ユーザー一覧", 10, plusOnersTable);
    addPanelEventHandler();
  }


  /*
   * フィルターログの文言を設定する
   */
  private String setFilterLog(String filterLog, String headermsg) {
    String result;
    if (filterLog.length() > 0) {
      result = headermsg + filterLog;
    } else {
      result = "";
    }
    return result;
  }

  /*
   * フィルターイベントを発生させる
   */
  private void fireFilterEvent() {
    eventListener.onFilter(activityFilterLog + " " + plusOnerFilterLog);
  }

  /*
   * テーブル上でのイベント通知ハンドラの設定（フィルタボタン）
   */
  private void addTableEventHandler() {
    plusOnersTable.addItemEventListener(new ItemEventListener<PlusPeople>() {
      @Override
      public void onEvent(PlusPeople item) {
        activityTablePanel.doPlusOnerFilter(item.getId(), item.getDisplayName());
      }
    });
    activityTable.addItemEventListener(new ItemEventListener<PlusActivity>() {
      @Override
      public void onEvent(PlusActivity item) {
        plusOnersTablePanel.doActivityFilter(item.getPlusOnerIds(), shortenName(item, 6));
      }
    });
  }

  /*
   * パネル上でのイベント通知ハンドラの設定（フィルター文言の入力）
   */
  private void addPanelEventHandler() {
    activityTablePanel.addEventListener(new TableEventListener() {
      @Override
      public void onFilter(String filterLog) {
        activityFilterLog = setFilterLog(filterLog, "activity ");
        fireFilterEvent();
      }
    });
    plusOnersTablePanel.addEventListener(new TableEventListener() {
      @Override
      public void onFilter(String filterLog) {
        plusOnerFilterLog = setFilterLog(filterLog, "+1er ");
        fireFilterEvent();
      }
    });
  }

  /*
   * タイトルを指定文字数以内に切り取る
   */
  private String shortenName(PlusActivity source, int maxlength) {
    String shortenTitle = source.getTitle();
    if (source.getTitle().length() > maxlength) {
      shortenTitle = source.getTitle().substring(0, maxlength) + "...";
    }
    return shortenTitle + "(" + source.getId() + ")";
  }
}
