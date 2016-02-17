package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * google+アイテムデータ表示するcellTable
 * 
 * @author LeoPanda
 *
 */
public abstract class SimpleListTable<T> extends CellTable<T> {
	private ListDataProvider<T> dataProvider; // テーブルへのデータプロバイダ
	protected List<ColumnSet> columSets = new ArrayList<ColumnSet>();
	protected ListHandler<T> sortHandler; // カラムのソートハンドラ
	protected final SingleSelectionModel<T> selectionModel = new SingleSelectionModel<T>();// 選択された行のデータ・モデルƒ
	protected SelectionChangeEvent.Handler selectionChangeHandler; // 選択ハンドラ

	public SimpleListTable(List<T> items) {
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
	 * テーブルのイニシャライズ
	 */
	private void initializeTable() {
		this.dataProvider = new ListDataProvider<T>();
		// this.columns = new HashMap<String, Column<T, ?>>();
		this.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		this.setSelectionModel(selectionModel);
		sortHandler = new ListHandler<T>(dataProvider.getList());
	}
	/**
	 * 受信したデータを表示する
	 * 
	 * @param itemList
	 */
	private void displayLines(List<T> itemList) {
		// テーブルにデータプロバイダを接続する
		this.dataProvider.addDataDisplay(this);
		// テーブルの表示幅を指定する
		this.setWidth(getWidth());
		for (ColumnSet Set : columSets) {
			// カラムの表示位置と表示幅を指定する
			Set.colum.setHorizontalAlignment(Set.alignment);
			this.setColumnWidth(Set.colum, String.valueOf(Set.columWidth)
					+ "px");
			// 表示カラムをテーブルに追加する
			this.addColumn(Set.colum, Set.columName);
		}
		// データプロバイダに明細データをセットする
		List<T> dataProviderList = dataProvider.getList();
		for (T item : itemList) {
			dataProviderList.add(item);
		}
		// カラムのソート条件をセットする
		setSortHandler();
		// ソートハンドラをテーブルに接続する
		this.addColumnSortHandler(sortHandler);
	};
	/**
	 * カラム情報セットを作成する
	 * 
	 * @param columName
	 * @param colum
	 * @param columWidth
	 * @param alignment
	 * @return
	 */
	public ColumnSet newColumnSet(String columName, Column<T, ?> colum,
			int columWidth, HorizontalAlignmentConstant alignment) {
		alignment = (alignment == null)
				? HasHorizontalAlignment.ALIGN_DEFAULT
				: alignment;
		return new ColumnSet(columName, colum, columWidth, alignment);
	}
	/**
	 * カラムの表示幅を合計してテーブルの表示幅を返す
	 * 
	 * @return
	 */
	public String getWidth() {
		int result = 0;
		for (ColumnSet Set : columSets) {
			result += Set.columWidth;
		}
		return String.valueOf(result) + "px";
	}
	/**
	 * テーブルに表示する項目のカラムデータ（Column） をnewし columnSetへaddする処理を記述する。
	 */
	abstract void setColumns();
	/**
	 * カラムソートハンドラのソート条件を記述する column.setSortable(true);
	 * sortHandler.setComparator(column,new comparator<T>{});
	 * this.getColumnSortList().push(Column);
	 */
	abstract void setSortHandler();
	/**
	 * to be new selectionChangeHanlder here.
	 */
	abstract void setSelectionChangeHandler();

	/**
	 * 表示するカラム情報セットを保持するクラス
	 * 
	 * @author LeoPanda
	 *
	 */
	private class ColumnSet {
		String columName;
		Column<T, ?> colum;
		int columWidth;
		HorizontalAlignmentConstant alignment;
		ColumnSet(String columName, Column<T, ?> colum, int columWidth,
				HorizontalAlignmentConstant alignment) {
			this.columName = columName;
			this.colum = colum;
			this.columWidth = columWidth;
			this.alignment = alignment;
		}
	}
}
