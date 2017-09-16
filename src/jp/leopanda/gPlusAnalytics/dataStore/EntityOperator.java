package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.List;

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
    List<Entity> entities = new EntitiesProceccer<List<Entity>>(new ArrayList<Entity>()) {
      @Override
      void whenNoEntity() {
        entities.add(newEntity());
      }
      @Override
      void doEachEntity(Entity entity) {
        entities.add(entity);
      }
    }.exec().getEntities();

    return entities;
  }


  /**
   * データストアからEntityを削除する
   */
  public void removeAllEntites() {
    this.isNew = true;
    new EntitiesProceccer<Void>() {
      @Override
      void doEachEntity(Entity entity) {
        ds.delete(entity.getKey());        
      }
      @Override
      void whenNoEntity() {
      }
    }.exec();
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
   * Queryを実行してデータストアからEntityのリストを取得し
   * 各Entity毎に処理を行わせるための抽象クラス
   * @author LeoPanda
   *
   * @param <I>
   */
  private abstract class EntitiesProceccer<I> {
    PreparedQuery pq = getPreparedQuery();
    QueryResultList<Entity> resultList = pq.asQueryResultList(FetchOptions.Builder.withDefaults());
    I entities; //ループ内処理の結果を保持するためのスタックエリア

    /**
     * ループ内の処理結果を出力する場合のコンストラクタ
     * @param entities
     */
    EntitiesProceccer(I entities) {
      this.entities = entities;
    }

    /**
     * コンストラクタ
     */
    EntitiesProceccer() {
    }

    /**
     * 処理の実行
     * @return
     */
    EntitiesProceccer<I> exec() {
      if (resultList.size() > 0) {
        for (Entity entity : resultList) {
          doEachEntity(entity);
        }
      } else {
        whenNoEntity();
      }
      return this;
    }

    /**
     * 処理結果を取り出す
     * @return
     */
    I getEntities() {
      return this.entities;
    }
    /**
     * 取得した各Entity毎の処理
     * @param entity
     */
    abstract void doEachEntity(Entity entity);

    /**
     *Entityがない場合の処理 
     */
    abstract void whenNoEntity();
  }

}
