package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import jp.leopanda.gPlusAnalytics.client.Statics;

/**
 * cellTableの基本クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class SimpleCellTable<I> extends CellTable<I> {
  private ListDataProvider<I> dataProvider; // テーブルへのデータプロバイダ
  protected List<ColumnSet> columSets = new ArrayList<ColumnSet>();
  protected ListHandler<I> sortHandler; // カラムのソートハンドラ
  protected final SingleSelectionModel<I> selectionModel = new SingleSelectionModel<I>();// 選択された行のデータ・モデル
  protected SelectionChangeEvent.Handler selectionChangeHandler; // 選択ハンドラ
  protected ColumnSortEvent.Handler sortEventHander;

  /**
   * コンストラクタ
   * 
   * @param items
   *          表示するアイテムデータ
   */
  public SimpleCellTable(List<I> items) {
    // 初期化
    initializeTable();
    setSelectionChangeHandler();
    if (selectionChangeHandler != null) {
      selectionModel.addSelectionChangeHandler(selectionChangeHandler);
    }
    setColumns();
    // 明細の表示
    displayLines(items);
  }

  /**
   * 表示データの操作ハンドルのリストを取得する
   * 
   * @return 表示データを直接操作できるハンドルリスト
   */
  public List<I> getDisplayList() {
    return dataProvider.getList();
  }

  /*
   * テーブルのイニシャライズ
   */
  private void initializeTable() {
    this.dataProvider = new ListDataProvider<I>();
    this.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
    this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
    this.setSelectionModel(selectionModel);
    sortHandler = new ListHandler<I>(dataProvider.getList()) {
      /*
       * ソートイベント発生時のイベントリスナーを外出し
       */
      @Override
      public void onColumnSort(ColumnSortEvent event) {
        super.onColumnSort(event);
        sortEventHander.onColumnSort(event);
      }
    };
  }

  /*
   * 受信したデータを表示する
   */
  private void displayLines(List<I> items) {
    // テーブルにデータプロバイダを接続する
    this.dataProvider.addDataDisplay(this);
    // テーブルの表示幅を指定する
    this.setWidth(getWidth());
    for (ColumnSet columSet : columSets) {
      // カラムの表示位置と表示幅を指定する
      columSet.colum.setHorizontalAlignment(columSet.alignment);
      this.setColumnWidth(columSet.colum, Statics.getLengthWithUnit(columSet.columWidth));
      // 表示カラムをテーブルに追加する
      this.addColumn(columSet.colum, columSet.columName);
    }
    // アイテムデータをロードする
    loadItems(items);
    // カラムのソート条件をセットする
    setSortHandler();
    // ソートハンドラをテーブルに接続する
    this.addColumnSortHandler(sortHandler);
  }

  /*
   * アイテムデータをロードする
   */
  private void loadItems(List<I> items) {
    List<I> displayList = dataProvider.getList();
    for (I item : items) {
      displayList.add(item);
    }
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
   * カラム情報セットを作成する
   * 
   * @param columName
   *          カラムの名称
   * @param colum
   *          カラムオブジェクト
   * @param columWidth
   *          カラムの表示幅
   * @param alignment
   *          カラムの表示修飾
   * @return 作成されたカラム情報セット
   */
  public ColumnSet newColumnSet(String columName, Column<I, ?> colum, int columWidth,
      HorizontalAlignmentConstant alignment) {
    alignment = (alignment == null) ? HasHorizontalAlignment.ALIGN_DEFAULT : alignment;
    return new ColumnSet(columName, colum, columWidth, alignment);
  }

  /**
   * カラムの表示幅を合計してテーブルの表示幅を返す
   * 
   * @return テーブルの表示幅
   */
  public String getWidth() {
    int result = 0;
    for (ColumnSet columSet : columSets) {
      result += columSet.columWidth;
    }
    return Statics.getLengthWithUnit(result);
  }

  /*
   * テーブルに表示する項目のカラムデータ（Column） をnewし columnSetへaddする処理を記述する。
   */
  protected abstract void setColumns();

  /*
   * カラムソートハンドラのソート条件を記述する
   */
  protected abstract void setSortHandler();

  /*
   * 行選択ハンドラの処理を記述する
   */
  protected abstract void setSelectionChangeHandler();

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
