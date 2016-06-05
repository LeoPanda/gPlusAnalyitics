package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

import jp.leopanda.gPlusAnalytics.client.Formatter;
import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ItemFilter;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ItemListPop;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 同一投稿日のアクテビティを選択させるポップアップ画面
 * 
 * @author LeoPanda
 *
 */
public class ActivitySelectorPop extends ItemListPop<PlusActivity, ActivityTableMini, Date> {

  /**
   * コンストラクタ
   * 
   * @param published アクテビティの投稿日付
   */
  public ActivitySelectorPop(Date published) {
    super(DateTimeFormat.getFormat(DateFormat.YYMMDD.getValue()).format(published) + "のアクテビティ", 10,
        new ActivityTableMini(Global.getActivityItems()), published,
        new ItemFilter<PlusActivity, Date>() {
          @Override
          // フィルター条件
              public
              boolean compare(PlusActivity item, Date published) {
            Formatter formatter = new Formatter();
            return formatter.getYYMMDDString(item.published).equals(
                formatter.getYYMMDDString(published));

          }
        });
  }

}
