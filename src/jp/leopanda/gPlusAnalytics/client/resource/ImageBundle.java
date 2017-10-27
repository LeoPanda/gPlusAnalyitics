package jp.leopanda.gPlusAnalytics.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author LeoPanda
 *
 */
public interface ImageBundle extends ClientBundle {
  ImageBundle INSTANCE = GWT.create(ImageBundle.class);

  @Source("loading.gif")
  ImageResource loading();

  @Source("gears.gif")
  ImageResource gears();

  @Source("back.gif")
  ImageResource back();

  @Source("forward.gif")
  ImageResource forward();

  @Source("next.gif")
  ImageResource next();

  @Source("reward.gif")
  ImageResource reward();

}
