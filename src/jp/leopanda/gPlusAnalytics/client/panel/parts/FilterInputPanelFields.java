package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.Optional;
import java.util.function.Function;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.client.enums.ItemType;
import jp.leopanda.gPlusAnalytics.client.enums.Month;
import jp.leopanda.gPlusAnalytics.client.util.NumOfPlusOneFilterKeyword;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;
import jp.leopanda.gPlusAnalytics.interFace.FilterRequestListener;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.FieldCommon;
import jp.leopanda.panelFrame.filedParts.ListBoxField;
import jp.leopanda.panelFrame.filedParts.TextBoxField;
import jp.leopanda.panelFrame.validate.BlankValidator;
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
  TextBoxField activityFilter = new TextBoxField("アクテビティ:",
      new ValidateBase[] { new BlankValidator() });
  PublishedYearListBox publishedYear;
  PostCategoryListBox postCategory;
  ListBoxField publishedMonth = new ListBoxField(" ", null, Month.values());
  NumOfPlusOneComboField plusOnerPlusOnes = new NumOfPlusOneComboField();
  NumOfPlusOneComboField activityPlusOnes = new NumOfPlusOneComboField();
  // リクエストリスナー
  FilterRequestListener requestListener;

  /**
   * コンストラクタ
   * 
   * @param requestListener
   */
  public FilterInputPanelFields(FilterableSourceItems sourceItems) {
    setEnterKeyPressHandler();
    postCategory = new PostCategoryListBox("投稿先:", sourceItems.getActivities());
    publishedYear = new PublishedYearListBox("投稿日付:", sourceItems.getActivities());
    postCategory.addEventListener(getOnValueChange(FilterType.ACTIVITIES_BY_ACCESSDESCRIPTION));
    publishedYear.addEventListener(getOnValueChange(FilterType.ACTIVITIES_BY_PUBLISHED_YEAR));
    publishedMonth.addEventListener(getOnValueChange(FilterType.ACTIVITIES_BY_PUBLISHED_MONTH));
  }

  /**
   * 入力フィールド内でEnterキーが押された時の動作を設定する
   */
  private void setEnterKeyPressHandler() {
    plusOnerFilter.getBasicField()
        .addKeyPressHandler(getEnterKeyHandler(FilterType.PLUSONERS_BY_KEYWORD));
    activityFilter.getBasicField()
        .addKeyPressHandler(getEnterKeyHandler(FilterType.ACTIVITIES_BY_KEYWORD));
    plusOnerPlusOnes.addKeyPressHandler(getEnterKeyHandler(FilterType.PLUSONERS_BY_NUMOFPLUSONE));
    activityPlusOnes.addKeyPressHandler(getEnterKeyHandler(FilterType.ACTIVITIES_BY_NUMOFPLUSONE));
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

  public NumOfPlusOneComboField getPlusOnerPlusOnes() {
    return plusOnerPlusOnes;
  }

  public NumOfPlusOneComboField getActivityPlusOnes() {
    return activityPlusOnes;
  }

  /**
   * リストボックス値変更時のアクションハンドラを返す
   * 
   * @param filterType
   * @return
   */
  private EventAction getOnValueChange(final FilterType filterType) {
    return () -> requestListener.request(filterType, getFilterKeyword(filterType));
  }

  /**
   * 入力フィールド内でエンターキーが押された場合にフィルターを実行する
   * 
   * @param filterType
   * @return
   */
  private KeyPressHandler getEnterKeyHandler(final FilterType filterType) {
    return event -> {
      int keyCode = getKeycode(event);
      if (keyCode == KeyCodes.KEY_ENTER) {
        requestFilter(filterType);
      }
    };
  }

  /**
   * キーワードを取得し選択されたフィルターをリクエストする
   * 
   * @param filterType
   */
  private void requestFilter(final FilterType filterType) {
    Optional<Object> keywordValue = Optional.ofNullable(getFilterKeyword(filterType));
    keywordValue.ifPresent(keyword -> requestListener.request(filterType, keyword));
  }

  /**
   * 押されたキーのコードを取得する
   * 
   * @param event
   * @return
   */
  private int getKeycode(KeyPressEvent event) {
    final int fireFoxKeyCode = 0;
    int keyCode = event.getUnicodeCharCode();
    keyCode = (keyCode == fireFoxKeyCode) ? event.getNativeEvent().getKeyCode() : keyCode;
    return keyCode;
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
      case PLUSONERS_BY_KEYWORD:
        keyword = validateAndGetKeyWord(plusOnerFilter, field -> field.getText());
        break;
      case ACTIVITIES_BY_KEYWORD:
        keyword = validateAndGetKeyWord(activityFilter, field -> field.getText());
        break;
      case ACTIVITIES_BY_PUBLISHED_YEAR:
        keyword = (String) publishedYear.getValue();
        break;
      case ACTIVITIES_BY_PUBLISHED_MONTH:
        keyword = (String) Month.values()[publishedMonth.getSelectedIndex()].getNumber();
        break;
      case ACTIVITIES_BY_ACCESSDESCRIPTION:
        keyword = (String) postCategory.getValue();
        break;
      case PLUSONERS_BY_NUMOFPLUSONE:
        keyword = validateAndGetKeyWord(plusOnerPlusOnes.getField(),
            field -> new NumOfPlusOneFilterKeyword(plusOnerPlusOnes.getKeyWord(),
                plusOnerPlusOnes.getcompOperator(), ItemType.PLUSONERS));
        break;
      case ACTIVITIES_BY_NUMOFPLUSONE:
        keyword = validateAndGetKeyWord(activityPlusOnes.getField(),
            field -> new NumOfPlusOneFilterKeyword(activityPlusOnes.getKeyWord(),
                activityPlusOnes.getcompOperator(), ItemType.ACTIVITIES));
        break;
      default:
        break;
    }
    return keyword;
  }

  /**
   * フィールドのバリデートとkeywordの取り出し
   * 
   * @param field
   * @param keywordSetter
   * @return
   */
  private Object validateAndGetKeyWord(FieldCommon field,
      Function<FieldCommon, Object> keywordSetter) {
    if (field.validate()) {
      return keywordSetter.apply(field);
    } else {
      field.popError();
    }
    return null;
  }

}