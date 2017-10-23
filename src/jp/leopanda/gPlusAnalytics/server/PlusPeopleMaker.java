package jp.leopanda.gPlusAnalytics.server;

import java.util.Optional;
import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * Google API PersonからPlus Peopleデータを作成する
 * @author LeoPanda
 *
 */
public class PlusPeopleMaker {
  Logger logger = Logger.getLogger("PlusOnerMaker");

  /**
   * 　PlusPeopleの生成
   * @param person
   * @return
   */
  public PlusPeople get(com.google.api.services.plus.model.Person person) {
    PlusPeople plusPeople = new PlusPeople();
    plusPeople.id = person.getId();
    plusPeople.url = person.getUrl();
    plusPeople.displayName = person.getDisplayName();
    Optional.ofNullable(person.getImage()).ifPresent(image ->
      plusPeople.setImageUlr(Optional.ofNullable(image.getUrl()).orElse("")));
    return plusPeople;
  }
}
