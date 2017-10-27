package jp.leopanda.gPlusAnalytics.client.enums;

import com.google.gwt.resources.client.ImageResource;
import jp.leopanda.gPlusAnalytics.client.resource.ImageBundle;

/**
 * 画像リソース
 * 
 * @author LeoPanda
 *
 */
public enum Images {
  LOADING(ImageBundle.INSTANCE.loading()), 
  GEARS(ImageBundle.INSTANCE.gears()),
  NEXT_BUTTON(ImageBundle.INSTANCE.next()),
  BACK_BUTTON(ImageBundle.INSTANCE.back()),
  FORWARD_BUTTON(ImageBundle.INSTANCE.forward()),
  REWARD_BUTTON(ImageBundle.INSTANCE.reward());
  
  ImageResource imageResource;

  Images(ImageResource imageResource) {
    this.imageResource = imageResource;
  }

  public ImageResource get() {
    return imageResource;
  }
}
