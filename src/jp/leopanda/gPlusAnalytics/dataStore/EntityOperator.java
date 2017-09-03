package jp.leopanda.gPlusAnalytics.dataStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * DataStore操作クラス 各エンティティはひとつのActorIDに対して１レコードのみ生成されることを前提としています。
 * 
 * @author LeoPanda
 *
 */
public class EntityOperator {
  private DatastoreService ds;
  private CommonProperty property;
  private Entity entity;
  private String actorId;
  private boolean isNew = true;

  /**
   * コンストラクタ
   */
  public EntityOperator(DatastoreService ds, CommonProperty property, String actiorId) {
    this.ds = ds;
    this.property = property;
    this.actorId = actiorId;
  }

  /**
   * エンティティのプロパティ名定義を取得する
   * 
   * @return
   */
  public CommonProperty getPropertyName() {
    return this.property;
  }

  /**
   * エンティティのプロパティを取得する
   * 
   * @param propertyName
   * @return
   */
  public Object getProperty(String propertyName) {
    return entity.getProperty(propertyName);
  }

  /**
   * エンティティにプロパティをセットする
   * 
   * @param propertyName
   * @param value
   */
  public void setProperty(String propertyName, Object value) {
    entity.setProperty(propertyName, value);
  }

  /**
   * Entityを単独で取得する
   * 
   * @return
   */
  public Entity getEntity() {
    this.isNew = false;
    PreparedQuery pq = getPreparedQuery();
    int countEntites = getEntityCount(pq);
    if (countEntites == 0) {
      newEntity();
    } else if (countEntites == 1) {
      entity = pq.asSingleEntity();
    } else { // 同一ActorIDのエンティティが複数生成されている状態はイレギュラーなので初期化する
      for (Entity entity : pq.asIterable()) {
        ds.delete(entity.getKey());
      }
      newEntity();
    }
    return entity;
  }

  /**
   * EntityをListで取得する
   * 
   * @return
   */
  public List<Entity> getEntityAsList() {
    this.isNew = false;
    List<Entity> entities = new ArrayList<Entity>();
    PreparedQuery pq = getPreparedQuery();
    if (getEntityCount(pq) == 0) {
      entities.add(newEntity());
    } else {
      for (Entity entity : pq.asIterable()) {
        entities.add(entity);
      }
    }
    return entities;
  }

  /**
   * データストアからEntityを削除する
   */
  public void removeAllEntites() {
    PreparedQuery pq = getPreparedQuery();
    if (getEntityCount(pq) > 0) {
      for (Entity entity : pq.asIterable()) {
        ds.delete(entity.getKey());
      }
    }
    this.isNew = true;
  }

  /**
   * ストアされているエンティティの数を取得する
   * 
   * @param pq
   * @return
   */
  private int getEntityCount(PreparedQuery pq) {
    return pq.countEntities(FetchOptions.Builder.withDefaults());
  }

  /**
   * entityをセットする
   * 
   * @param entity
   */
  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  /**
   * エンティティがすでにDataStoreに存在するかどうかのチェック
   * 
   * @return
   */
  public boolean isNew() {
    return this.isNew;
  }

  /**
   * Entityを書き込む
   */
  public void putEntity() {
    setProperty(property.PUBLISED, getCurrentDate());
    ds.put(this.entity);
  }

  /**
   * エンティティを新規生成する
   */
  public Entity newEntity() {
    entity = new Entity(property.getKindName());
    entity.setProperty(property.ACTOR_ID, actorId);
    this.isNew = true;
    return entity;
  }

  /**
   * 共通エンティティ用のPreparedQueryを取得する
   * 
   * @return
   */
  private PreparedQuery getPreparedQuery() {
    Query query = new Query(property.getKindName());
    query.setFilter(new FilterPredicate(property.ACTOR_ID, FilterOperator.EQUAL, actorId));
    return ds.prepare(query);
  }

  /**
   * 当日日付の取得
   * 
   * @return
   */
  private String getCurrentDate() {
    final String dateFormat = "yyyy-MM-dd-HH-mm-ss";
    final String timeZone = "Asia/Tokyo";
    TimeZone timezone = TimeZone.getTimeZone(timeZone);
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    formatter.setTimeZone(timezone);
    return formatter.format(new Date());
  }

}
