package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.QueryResultList;

import jp.leopanda.gPlusAnalytics.server.ServerUtil;

/**
 * DataStore Entity操作クラス
 * 
 * @author LeoPanda
 *
 */
public class EntityOperator {
  private DatastoreService ds;
  private StoredItemProperty property;
  private Entity entity;
  private String actorId;
  private boolean isNew = true;

  /**
   * コンストラクタ
   */
  public EntityOperator(DatastoreService ds, StoredItemProperty property, String actiorId) {
    this.ds = ds;
    this.property = property;
    this.actorId = actiorId;
  }

  /**
   * エンティティのプロパティ名定義を取得する
   * 
   * @return
   */
  public StoredItemProperty getPropertyName() {
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
   * EntityをListで取得する
   * 
   * @return
   */
  public List<Entity> getEntityAsList() {
    this.isNew = false;
    List<Entity> entities = new ArrayList<Entity>();
    processEachEntities(entity -> entities.add(entity), () -> entities.add(newEntity()));
    return entities;
  }

  /**
   * データストアからEntityを削除する
   */
  public void removeAllEntites() {
    this.isNew = true;
    processEachEntities(entity -> ds.delete(entity.getKey()), () -> null);
  }

  /**
   * データストアからエンティティを取り出して処理する
   * 
   * @param action エンティティ毎の処理
   * @param isEmpty エンティティが見つからなかった場合の処理
   */
  private void processEachEntities(Consumer<Entity> action, Supplier<?> isEmpty) {
    QueryResultList<Entity> resultList = getPreparedQuery()
        .asQueryResultList(FetchOptions.Builder.withDefaults());
    if (resultList.size() > 0) {
      resultList.forEach(entity -> action.accept(entity));
    } else {
      isEmpty.get();
    }
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
    setProperty(property.PUBLISED, ServerUtil.getCurrentDate());
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

}
