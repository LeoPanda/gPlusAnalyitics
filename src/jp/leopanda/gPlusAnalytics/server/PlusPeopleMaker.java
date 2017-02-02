package jp.leopanda.gPlusAnalytics.server;

import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.dataObject.AttachmentImage;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * @author LeoPanda
 *
 */
public class PlusPeopleMaker {
  Logger logger = Logger.getLogger("PlusOnerMaker");

  public PlusPeople get(com.google.api.services.plus.model.Person person) {
    PlusPeople plusPeople = new PlusPeople();
    plusPeople.id = person.getId();
    plusPeople.url = person.getUrl();
    plusPeople.gender = person.getGender();
    plusPeople.language = person.getLanguage();
    plusPeople.displayName = person.getDisplayName();
    if (person.getImage() == null) {
      plusPeople.image = null;
    } else {
      plusPeople.image = new AttachmentImage();
      plusPeople.image.url = person.getImage().getUrl();
    }
    return plusPeople;
  }
}
