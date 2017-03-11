package jp.leopanda.gPlusAnalytics.client.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.dataObject.FilterableSourceItems;

/**
 * フィルターログ表示パネル
 * @author LeoPanda
 *
 */
public class FilterLogPanel extends HorizontalPanel{
  FilterableSourceItems sourceItems;
  Label logLabel = new Label();
  
  public FilterLogPanel(FilterableSourceItems sourceItems){
    this.sourceItems = sourceItems;

    logLabel.setTitle("フィルターログ");
    logLabel.setStyleName(MyStyle.FILTER_TEXT.getStyle());

    this.add(logLabel);
    
    displayLog();
    
  }
  
  /**
   * ログの表示
   */
  public void displayLog(){
    String logWord = sourceItems.getFilterLog();
    logLabel.setText(logWord.length() > 0 ? "フィルター履歴:"+ logWord:"");
  }

  
  
}
