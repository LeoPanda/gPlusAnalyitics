package jp.leopanda.gPlusAnalytics.client.panel.parts;

import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

import jp.leopanda.gPlusAnalytics.client.enums.CompOperator;
import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.panelFrame.filedParts.FieldCommon;
import jp.leopanda.panelFrame.filedParts.ListBoxField;
import jp.leopanda.panelFrame.filedParts.TextBoxField;
import jp.leopanda.panelFrame.validate.BlankValidator;
import jp.leopanda.panelFrame.validate.IntegerValidator;
import jp.leopanda.panelFrame.validate.ValidateBase;

/**
 * @author LeoPanda
 *
 */
public class NumOfPlusOneComboField {

  ListBoxField compOperator = new ListBoxField("", null, CompOperator.values());
  TextBoxField filter = new TextBoxField("",
      new ValidateBase[] { new IntegerValidator(), new BlankValidator() });

  /**
   * コンストラクタ
   */
  public NumOfPlusOneComboField(){
    filter.addStyleName(MyStyle.FILTER_NUMERIC.getStyle());
  }
  
  /**
   * 入力パネルを提供する
   * @return
   */
  public HorizontalPanel getPanel(){
   HorizontalPanel panel = new HorizontalPanel();
   panel.add(compOperator);
   panel.add(filter);
   return panel;
  }
  
  /**
   * キーボード入力時のハンドラを設定する
   * 
   * @param handler
   */
  public void addKeyPressHandler(KeyPressHandler handler) {
    filter.getBasicField().addKeyPressHandler(handler);
  }

  /**
   * 入力されたキーワードを提供する
   * 
   * @return
   */
  public int getKeyWord() {
    return Integer.valueOf(filter.getText());
  }

  /**
   * 入力された演算子を提供する
   * 
   * @return
   */
  public CompOperator getcompOperator() {
    return CompOperator.values()[compOperator.getSelectedIndex()];
  }
  
  /**
   * バリデータ用のフィールド共通要素を提供する
   * @return
   */
  public FieldCommon getField(){
    return this.filter;
  }
}
