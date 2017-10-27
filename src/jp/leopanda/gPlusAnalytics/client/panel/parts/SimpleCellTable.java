package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import jp.leopanda.gPlusAnalytics.client.Statics;

/**
 * cellTableの基本クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class SimpleCellTable<I> extends CellTable<I> {
  private ListDataProvider<I> dataProvider;
  private List<ColumnSet> columSets = new ArrayList<ColumnSet>();
  private ListHandler<I> sortHandler;
  private final SingleSelectionModel<I> selectionModel = new SingleSelectionModel<I>();// 選択された行のデータ・モデル
  private ColumnSortEvent.Handler sortEventHander;

  /**
   * コンストラクタ
   * 
   * @param items
   */
  public SimpleCellTable(List<I> items) {
    initializeTable();
    setColumnField();
    sortHandler = getSortHandler();
    displayLines(items);
  }

  /**
   * テーブルの初期化
   */
  private void initializeTable() {
    dataProvider = new ListDataProvider<I>();
    setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
    setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
    setSelectionModel(selectionModel);
  }

  /**
   * 表示するカラムを設定する
   */
  private void setColumnField() {
    setColumns();
    sortHandler = getSortHandler();
  };

  /**
   * 表示したいカラムを設定する
   */
  abstract protected void setColumns();

  /**
   * アイテムのソートハンドラを生成する
   * 
   * @param sortSetter
   * @return
   */
  private ListHandler<I> getSortHandler() {
    ListHandler<I> sortHandler = new ListHandler<I>(dataProvider.getList()) {
      @Override
      public void onColumnSort(ColumnSortEvent event) {
        super.onColumnSort(event);
        Optional.ofNullable(sortEventHander)
            .ifPresent(eventHandler -> eventHandler.onColumnSort(event));
      }
    };
    return sortHandler;
  }

  /**
   * 行選択アクションハンドラを設定する
   * 
   * @param selectionHanlder
   */
  public void setSelectionHandler(Consumer<I> selectionHanlder) {
    selectionModel.addSelectionChangeHandler(event -> {
          setSelectionHandlerAction(selectionHanlder);
        });
  }

  /**
   * 行選択時のアクションを設定する
   * @param selectionHanlder
   */
  private void setSelectionHandlerAction(Consumer<I> selectionHanlder) {
    Optional.ofNullable(selectionModel.getSelectedObject())
        .ifPresent(selectedItem -> {
          selectionModel.setSelected(selectedItem, false);
          selectionHanlder.accept(selectedItem);
        });
  }

  /**
   * カラムの表示幅を合計してテーブルの表示幅を返す
   * 
   * @return テーブルの表示幅
   */
  public String getWidth() {
    return Statics.getLengthWithUnit(
        columSets.stream().mapToInt(set -> set.columWidth).sum());
  }

  /**
   * 表示データの操作ハンドルのリストを取得する
   * 
   * @return 表示データを直接操作できるハンドルリスト
   */
  public List<I> getDisplayList() {
    return dataProvider.getList();
  }

  /**
   * アイテムデータ明細を表示する
   * 
   * @param items
   * @param sortSetter
   */
  private void displayLines(List<I> items) {
    dataProvider.addDataDisplay(this);
    setWidth(getWidth());
    columSets.forEach(columSet -> addColumnSetToTable(columSet));
    loadItems(items);
  }

  /**
   * カラムセットをテーブルに追加する
   * @param columSet
   */
  private void addColumnSetToTable(SimpleCellTable<I>.ColumnSet columSet) {
    columSet.colum.setHorizontalAlignment(columSet.alignment);
    setColumnWidth(columSet.colum, Statics.getLengthWithUnit(columSet.columWidth));
    addColumn(columSet.colum, columSet.columName);
  }

  /**
   * ソートハンドラのアクションを設定する
   * 
   * @param sortSetter
   */
  public void setSrotHandler(Consumer<ListHandler<I>> sortSetter) {
    Optional.ofNullable(sortSetter).ifPresent(setter -> setter.accept(sortHandler));
    addColumnSortHandler(sortHandler);
  }

  /**
   * アイテムのソートイベント発生時のアクションを設定する
   * 
   * @param sortEventHandler
   */
  public void setSortEventHandler(ColumnSortEvent.Handler sortEventHandler) {
    this.sortEventHander = sortEventHandler;
  }

  /**
   * アイテムデータをロードする
   * 
   * @param items
   */
  private void loadItems(List<I> items) {
    items.forEach(item -> dataProvider.getList().add(item));
  }

  /**
   * アイテムデータをロードし直す
   * 
   * @param items
   *          アイテムデータ
   */
  public void reLoadItems(List<I> items) {
    dataProvider.getList().clear();
    loadItems(items);
    this.setPageStart(0);
  }

  /**
   * 明細にコラムセットを追加する
   * 
   * @param columName
   * @param colum
   * @param columWidth
   * @param alignment
   * @return 作成されたカラム情報セット
   */
  public void addColumnSet(String columName, Column<I, ?> colum, int columWidth,
      HorizontalAlignmentConstant alignment) {
    columSets.add(newColumnSet(columName, colum, columWidth, alignment));
  }

  /**
   * カラム情報セットを作成する
   * 
   * @param columName
   * @param colum
   * @param columWidth
   * @param alignment
   * @return 作成されたカラム情報セット
   */
  private ColumnSet newColumnSet(String columName, Column<I, ?> colum, int columWidth,
      HorizontalAlignmentConstant alignment) {
    alignment = (alignment == null) ? HasHorizontalAlignment.ALIGN_DEFAULT : alignment;
    return new ColumnSet(columName, colum, columWidth, alignment);
  }

  /*
   * 表示するカラム情報セットを保持するインナークラス
   */
  private class ColumnSet {
    String columName;
    Column<I, ?> colum;
    int columWidth;
    HorizontalAlignmentConstant alignment;

    ColumnSet(String columName, Column<I, ?> colum, int columWidth,
        HorizontalAlignmentConstant alignment) {
      this.columName = columName;
      this.colum = colum;
      this.columWidth = columWidth;
      this.alignment = alignment;
    }
  }
}
