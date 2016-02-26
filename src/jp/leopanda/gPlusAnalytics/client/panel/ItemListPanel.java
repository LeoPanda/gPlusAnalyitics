package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Google+ API　アイテムデータの一覧テーブルパネル
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemListPanel<I extends PlusItem, T extends SimpleListTable<I>>
		extends
			VerticalPanel {

	protected List<I> itemList;
	protected SimpleListTable<I> itemTable;// 表示するアイテムデータのリスト
	private Label titleLabel;// パネルのタイトル名を表示するラベル
	private Label lineCountLabel;// アイテムの総数を表示するラベル
	private int pageSize = 0;// 表示するアイテムの行数
	protected int pageStart = 0;// 現在表示しているページ先頭行の位置
	private Button firstPageButton = new Button("<<");
	private Button lastPageButton = new Button(">>");
	private Button prevPageButton = new Button("<");
	private Button nextPageButton = new Button(">");
	// ページコントロール行ボタン間のスペース
	protected HorizontalPanel centerSpaceOfPageControl = new HorizontalPanel();
	/**
	 * コンストラクタ
	 * 
	 * @param itemList
	 *            表示するデータのリスト List<PlusItem>
	 * @param titleName
	 *            パネルのタイトル名
	 * @param pageSize
	 *            １ページに表示する行数
	 * @param itemTable
	 *            一覧表示するテーブルオブジェクト SimpleListTabaleを継承するクラスのインスタンス
	 */
	public ItemListPanel(List<I> items, String titleName, int pageSize,
			T itemTable) {
		this.itemTable = itemTable;
		this.pageSize = pageSize;
		this.itemList = items;

		// タイトル行の作成
		HorizontalPanel headerLine = makeTitleLine(titleName);
		this.add(headerLine);
		// ページコントロール行の作成
		HorizontalPanel pageControlLine = makePageControlLine();
		this.add(pageControlLine);
		// アイテム表示テーブルの追加
		itemTable.setPageSize(this.pageSize);
		this.add(itemTable);
		// 表示幅の設定
		this.setWidth(itemTable.getWidth());
		// カラムソート時に先頭行をリセット
		addSortEventHandler();
	}
	/**
	 * タイトル行クラスの作成
	 * 
	 * @return
	 */
	private HorizontalPanel makeTitleLine(String titleName) {
		HorizontalPanel headerLine = new HorizontalPanel();
		headerLine.setWidth(itemTable.getWidth());
		titleLabel = new Label(titleName);
		lineCountLabel = new Label("(" + String.valueOf(itemList.size()) + "件)");
		titleLabel.addStyleName(CssStyle.TitleLabel.getValue());
		lineCountLabel.addStyleName(CssStyle.CountLabel.getValue());
		headerLine.add(titleLabel);
		headerLine.add(lineCountLabel);
		return headerLine;
	}
	/**
	 * ページコントロール行パネルの作成
	 * 
	 * @param size
	 * @return
	 */
	private HorizontalPanel makePageControlLine() {
		addButtonClickHandlers();
		firstPageButton.addStyleName(CssStyle.PageButton.getValue());
		prevPageButton.addStyleName(CssStyle.PageButton.getValue());
		nextPageButton.addStyleName(CssStyle.PageButton.getValue());
		lastPageButton.addStyleName(CssStyle.PageButton.getValue());
		HorizontalPanel pageControlLine = new HorizontalPanel();
		pageControlLine.setWidth(itemTable.getWidth());
		HorizontalPanel leftSide = new HorizontalPanel();
		HorizontalPanel rightSide = new HorizontalPanel();
		leftSide.add(firstPageButton);
		leftSide.add(prevPageButton);
		rightSide.add(nextPageButton);
		rightSide.add(lastPageButton);
		pageControlLine.add(leftSide);
		pageControlLine.setHorizontalAlignment(ALIGN_LEFT);
		pageControlLine.add(centerSpaceOfPageControl);
		pageControlLine.setHorizontalAlignment(ALIGN_RIGHT);
		pageControlLine.add(rightSide);
		return pageControlLine;
	}
	/**
	 * 各ページコントロールボタンへクリックハンドラを追加する
	 */
	private void addButtonClickHandlers() {
		// 先頭ページボタン
		firstPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart = 0;
				itemTable.setPageStart(0);
			}
		});
		// 最終ページボタン
		lastPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart = itemList.size() - pageSize;
				itemTable.setPageStart(pageStart);
			}
		});
		// 前ページボタン
		prevPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart -= pageSize;
				pageStart = pageStart < 0 ? 0 : pageStart;

				itemTable.setPageStart(pageStart);
			}
		});
		// 次ページボタン
		nextPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart += pageSize;
				int lastPage = itemList.size();
				pageStart = pageStart + 1 > lastPage
						? pageStart - pageSize
						: pageStart;
				itemTable.setPageStart(pageStart);
			}
		});
	}
	/**
	 * セルテーブルのソートイベント発生時にテーブルの表示先頭行をリセット
	 */
	private void addSortEventHandler() {
		itemTable.sortEventHander = new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				pageStart = 0;
				itemTable.setPageStart(0);
			}
		};
	}
}
