/**
 * 
 */
package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.SelectionChangeEvent;

import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * @author LeoPanda
 *
 */
public class PlusOnersListTable extends SimpleListTable<PlusPeople>{

	/**コンストラクタ
	 * @param items
	 */
	public PlusOnersListTable(List<PlusPeople> items) {
		super(items);

	}
	SafeHtmlColumn<PlusPeople> imageColumn;
	TextColumn<PlusPeople> nameColumn;
	TextColumn<PlusPeople> plusOneColumn;

	@Override
	void setColumns() {
		//写真
		imageColumn = new SafeHtmlColumn<PlusPeople>() {		
			@Override
			public SafeHtml getValue(PlusPeople item) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendHtmlConstant("<img chartHeight=\"50\" chartWidth =\"50\" src=\"")
				.appendEscaped(item.getImageUrl()).appendHtmlConstant("\">");
				return builder.toSafeHtml();
			}
		};
		//ユーザー名
		nameColumn = new TextColumn<PlusPeople>() {
			@Override
			public String getValue(PlusPeople item) {
				return item.getDisplayName();
			}
		};
		//num of PlusOne
		plusOneColumn = new TextColumn<PlusPeople>() {
			@Override
			public String getValue(PlusPeople item) {
				return String.valueOf(item.getNumOfPlusOne());
			}
		};
		// カラム表示リストに登録
		this.columSets.add(newColumnSet("", imageColumn, 50, null));
		this.columSets.add(newColumnSet("名前", nameColumn, 200, null));
		this.columSets.add(newColumnSet("+1", plusOneColumn, 30, HasHorizontalAlignment.ALIGN_RIGHT));
	}

	/**
	 * カラムのソート条件をセットする
	 */
	@Override
	void setSortHandler() {
		plusOneColumn.setSortable(true);
		sortHandler.setComparator(plusOneColumn, new Comparator<PlusPeople>() {
			@Override
			public int compare(PlusPeople o1, PlusPeople o2) {
				return o1.getNumOfPlusOne() - o2.getNumOfPlusOne();
			}
		});
	}
	/**
	 * 行がクリックされたらg+プロファイル画面を表示する
	 */
	@Override
	void setSelectionChangeHandler() {
		this.selectionChangeHandler = new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				PlusPeople selected = selectionModel.getSelectedObject();
				if(selected != null){
					Window.open(selected.getUrl(), "PlusPeople", WindowOption.ItemDetail.getValue());
				}
			}
		};
	}

}
