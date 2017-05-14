package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.Month;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.ListBoxField;
import jp.leopanda.panelFrame.filedParts.TextBoxField;

/**
 * アイテムフィルター入力パネルのフィールドをセットアップする
 * 
 * @author LeoPanda
 *
 */
public class FilterInputPanelFields {
  // フィールド
  TextBoxField plusOnerFilter = new TextBoxField("+1er:", null);
  TextBoxField activityFilter = new TextBoxField("アクテビティ:", null);
  PublishedYearListBox publishedYear;
  PostCategoryListBox postCategory;
  ListBoxField publishedMonth = new ListBoxField(" ", null, Month.values());
  // リクエストリスナー
  FilterRequestListener requestListener;

  /**
   * コンストラクタ
   * 
   * @param requestListener
   */
  FilterInputPanelFields(FilterableSourceItems sourceItems) {
    plusOnerFilter.getBasicField()
        .addKeyPressHandler(getEnterKeyHandler(FilterType.PLUSONER_KEYWORD));
    activityFilter.getBasicField()
        .addKeyPressHandler(getEnterKeyHandler(FilterType.ACTIVITIES_KEYWORD));
    postCategory = new PostCategoryListBox("投稿先:", sourceItems.getActivities());
    publishedYear = new PublishedYearListBox("投稿日付:", sourceItems.getActivities());
    postCategory.addEventListener(getOnValueChange(FilterType.ACTIVITIES_ACCESSDESCRIPTION));
    publishedYear.addEventListener(getOnValueChange(FilterType.ACTIVITIES_PUBLISHED_YEAR));
    publishedMonth.addEventListener(getOnValueChange(FilterType.ACTIVITIES_PUBLISHED_MONTH));
  }

  /**
   * リクエストリスナーのセット
   * 
   * @param listener
   */
  public void setRequestListener(FilterRequestListener listener) {
    this.requestListener = listener;
  }

  // getters
  public TextBoxField getPlusOnerFilter() {
    return plusOnerFilter;
  }

  public TextBoxField getActivityFilter() {
    return activityFilter;
  }

  public PublishedYearListBox getPublishedYear() {
    return publishedYear;
  }

  public PostCategoryListBox getPostCategory() {
    return postCategory;
  }

  public ListBoxField getPublishedMonth() {
    return publishedMonth;
  }

  /**
   * リストボックス値変更時のアクションハンドラを返す
   * 
   * @param filterType
   * @return
   */
  private EventAction getOnValueChange(final FilterType filterType) {
    return new EventAction() {
      @Override
      public void onValueChange() {
        requestListener.request(filterType, getFilterKeyword(filterType));

      }
    };
  }

  /**
   * 入力フィールド内でエンターキーが押された場合の処理ハンドラを返す
   * 
   * @param filterType
   * @return
   */
  private KeyPressHandler getEnterKeyHandler(final FilterType filterType) {
    return new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {
        int keyCode = event.getUnicodeCharCode();
        if (keyCode == 0) {
          // Probably Firefox
          keyCode = event.getNativeEvent().getKeyCode();
        }
        if (keyCode == KeyCodes.KEY_ENTER) {
          requestListener.request(filterType, getFilterKeyword(filterType));
        }
      }
    };
  }

  /**
   * フィルター条件キーワードを取得する
   * 
   * @param filterType
   * @return
   */
  private String getFilterKeyword(FilterType filterType) {
    String keyword = null;
    switch (filterType) {
    case PLUSONER_KEYWORD:
      keyword = plusOnerFilter.getText();
      break;
    case ACTIVITIES_KEYWORD:
      keyword = activityFilter.getText();
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      keyword = publishedYear.getValue();
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      keyword = Month.values()[publishedMonth.getSelectedIndex()].getNumber();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      keyword = postCategory.getValue();
      break;
    default:
      break;
    }
    return keyword;
  }

}
