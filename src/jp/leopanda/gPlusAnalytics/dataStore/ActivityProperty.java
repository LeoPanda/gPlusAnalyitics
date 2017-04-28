package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.Date;

import com.google.appengine.api.datastore.PropertyProjection;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * データストア Activity エンティティ定義
 * 
 * @author LeoPanda
 *
 */
public enum ActivityProperty implements DataStoreProperty{
  KIND("Activity",String.class),ID("id", String.class), ACTOR_ID("actorId", String.class),
  PUBLISHED("published", Date.class), UPDATED("updated", Date.class),
  NUM_OF_PLUSONERS("numOfPlusOners", Integer.class),
  ACTIVITY_ITEM("activityItem", PlusActivity.class),
  PLUSONER_ITEMS("plusOnerItems", PlusPeople.class);

  private String name;
  private Class<?> clazz;

  /**
   * コンストラクタ
   * @param name
   * @param clazz
   */
  ActivityProperty(String name, Class<?> clazz) {
    this.name = name;
    this.clazz = clazz;
  }

  /*
   * Query結果に反映させるフィールドプロジェクションを返す
   */
@Override
  public PropertyProjection getProjection(){
    return new PropertyProjection(this.getName(), this.getClazz());
  }

  /* 
   * プロパティの名前を返す
   */
  @Override
  public String getName() {
    return name;
  }

  /* 
   * プロパティの属性クラスを返す
   */
  @Override
  public Class<?> getClazz() {
    return  clazz;
  }

}
