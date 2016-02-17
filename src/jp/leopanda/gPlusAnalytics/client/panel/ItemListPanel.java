package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.CssStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
public abstract class ItemListPanel<T1 extends PlusItem, T2 extends SimpleListTable<T1>>
		extends
			VerticalPanel {

	private List<T1> itemList;
	private SimpleListTable<T1> simpleItemTable;// 表示するアイテムデータのリスト
	private Label titleLabel;// パネルのタイトル名を表示するラベル
	private Label lineCountLabel;// アイテムの総数を表示するラベル
	private int pageSize = 0;// 表示するアイテムの行数
	private int pageStart = 0;// 現在表示しているページ先頭行の位置
	private Button firstPageButton = new Button("<<");
	private Button lastPageButton = new Button(">>");
	private Button prevPageButton = new Button("<");
	private Button nextPageButton = new Button(">");
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
	public ItemListPanel(List<T1> itemList, String titleName, int pageSize,
			T2 itemTable) {
		this.simpleItemTable = itemTable;
		this.pageSize = pageSize;
		this.itemList = itemList;

		// タイトル行の作成
		HorizontalPanel headerLine = makeTitleLine(titleName);
		this.add(headerLine);

		// ページコントロール行の作成
		HorizontalPanel pageControlLine = makePageControlLine();
		this.add(pageControlLine);
		// アイテム表示テーブルの追加
		simpleItemTable.setPageSize(this.pageSize);
		this.add(simpleItemTable);
		// 表示幅の設定
		this.setWidth(simpleItemTable.getWidth());
	}
	/**
	 * タイトル行クラス
	 * 
	 * @return
	 */
	private HorizontalPanel makeTitleLine(String titleName) {
		HorizontalPanel headerLine = new HorizontalPanel();
		headerLine.setWidth(simpleItemTable.getWidth());
		titleLabel = new Label(titleName);
		lineCountLabel = new Label("(" + String.valueOf(itemList.size()) + "件)");
		titleLabel.addStyleName(CssStyle.TitleLabel.getValue());
		lineCountLabel.addStyleName(CssStyle.CountLabel.getValue());
		headerLine.add(titleLabel);
		headerLine.add(lineCountLabel);
		return headerLine;
	}
	/**
	 * ページコントロール行クラス
	 * 
	 * @param size
	 * @return
	 */
	private HorizontalPanel makePageControlLine() {
		// 先頭ページボタン
		firstPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart = 0;
				simpleItemTable.setPageStart(0);
			}
		});
		// 最終ページボタン
		lastPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart = itemList.size() - pageSize;
				simpleItemTable.setPageStart(pageStart);
			}
		});
		// 前ページボタン
		prevPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart -= pageSize;
				pageStart = pageStart < 0 ? 0 : pageStart;

				simpleItemTable.setPageStart(pageStart);
			}
		});
		// 次ページボタン
		nextPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageStart += pageSize;
				int lastPage = itemList.size();
				pageStart = pageStart+1 > lastPage
						? pageStart - pageSize
						: pageStart;
				simpleItemTable.setPageStart(pageStart);
			}
		});
		firstPageButton.addStyleName(CssStyle.PageButton.getValue());
		prevPageButton.addStyleName(CssStyle.PageButton.getValue());
		nextPageButton.addStyleName(CssStyle.PageButton.getValue());
		lastPageButton.addStyleName(CssStyle.PageButton.getValue());
		HorizontalPanel pageControlLine = new HorizontalPanel();
		pageControlLine.setWidth(simpleItemTable.getWidth());
		HorizontalPanel leftSide = new HorizontalPanel();
		HorizontalPanel rightSide = new HorizontalPanel();
		leftSide.add(firstPageButton);
		leftSide.add(prevPageButton);
		rightSide.add(nextPageButton);
		rightSide.add(lastPageButton);
		pageControlLine.add(leftSide);
		pageControlLine.setHorizontalAlignment(ALIGN_RIGHT);
		pageControlLine.add(rightSide);
		return pageControlLine;
	}

}
