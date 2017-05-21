package jp.leopanda.gPlusAnalytics.client.cssResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author LeoPanda
 *
 */
public interface CssStyle extends ClientBundle {
  CssStyle INSTANCE = GWT.create(CssStyle.class);

  interface Style extends CssResource {
    String pageButton();

    String titleLabel();

    String countLabel();

    String photoCell();

    String filterButton();

    String filterLogCard();

    String filterLabel();

    String filterNumeric();

    String filterBoolean();

    String filterDisable();

    String resetButton();

    String tableText();

    String tablePhoto();

    String yearLabel();

    String monthLabel();

    String detailPopWindow();

    String detailPopLine();

    String cehckBoxLabel();
  }

  Style style();
}
