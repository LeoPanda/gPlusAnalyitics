package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

import jp.leopanda.gPlusAnalytics.client.enums.CompOperator;
import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.Month;
import jp.leopanda.gPlusAnalytics.client.util.NumOfPlusOneFilterKeyword;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.FieldCommon;
import jp.leopanda.panelFrame.filedParts.ListBoxField;
import jp.leopanda.panelFrame.filedParts.TextBoxField;
import jp.leopanda.panelFrame.validate.BlankValidator;
import jp.leopanda.panelFrame.validate.IntegerValidator;
import jp.leopanda.panelFrame.validate.ValidateBase;

/**
 * アイテムフィルター入力パネルのフィールドをセットアップする
 * 
 * @author LeoPanda
 *
 */
public class FilterInputPanelFields {
  // フィールド
  TextBoxField plusOnerFilter = new TextBoxField("+1er:",
      new ValidateBase[] { new BlankValidator() });
  ListBoxField compOperator = new ListBoxField("", null, CompOperator.values());
  TextBoxField numOfPlusOneFilter = new TextBoxField("",
      new ValidateBase[] { new IntegerValidator(), new BlankValidator() });
  TextBoxField activityFilter = new TextBoxField("アクテビティ:",
      new ValidateBase[] { new BlankValidator() });
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
    numOfPlusOneFilter.getBasicField()
        .addKeyPressHandler(getEnterKeyHandler(FilterType.PLUSONER_NUMOFPLUSONE));
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

  public TextBoxField getNumOfPlusOneFilter() {
    return numOfPlusOneFilter;
  }

  public ListBoxField getCompOperator() {
    return compOperator;
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
          Object keyword = getFilterKeyword(filterType);
          if (keyword != null) {
            requestListener.request(filterType, keyword);
          }
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
  private Object getFilterKeyword(FilterType filterType) {
    Object keyword = null;
    switch (filterType) {
    case PLUSONER_KEYWORD:
      keyword = new FieldProcess(plusOnerFilter) {}.getKeyWord();
      break;
    case ACTIVITIES_KEYWORD:
      keyword = new FieldProcess(activityFilter) {}.getKeyWord();
      break;
    case ACTIVITIES_PUBLISHED_YEAR:
      keyword = (String) publishedYear.getValue();
      break;
    case ACTIVITIES_PUBLISHED_MONTH:
      keyword = (String) Month.values()[publishedMonth.getSelectedIndex()].getNumber();
      break;
    case ACTIVITIES_ACCESSDESCRIPTION:
      keyword = (String) postCategory.getValue();
      break;
    case PLUSONER_NUMOFPLUSONE:
      keyword = new FieldProcess(numOfPlusOneFilter) {
        @Override
        Object keywordSetter() {
          return new NumOfPlusOneFilterKeyword(Integer.valueOf(numOfPlusOneFilter.getText()),
              CompOperator.values()[compOperator.getSelectedIndex()]);
        }
      }.getKeyWord();
    default:
      break;
    }
    return keyword;
  }

  /**
   * フィールドからkeywordを取り出す定形処理
   * 
   * @author LeoPanda
   *
   */
  private abstract class FieldProcess {
    FieldCommon field;

    FieldProcess(FieldCommon field) {
      this.field = field;
    }

    /**
     * フィールドのバリデートチェックとキーワードの取り出し
     * @return
     */
    Object getKeyWord() {
      if (field.validate()) {
        return keywordSetter();
      } else {
        field.popError();
      }
      return null;
    }

    /**
     * キーワードの取り出しロジック
     * @return
     */
    Object keywordSetter(){
      return (String) field.getText();
    }
  }

}
