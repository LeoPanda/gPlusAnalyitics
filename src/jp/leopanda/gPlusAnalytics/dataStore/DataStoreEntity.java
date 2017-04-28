package jp.leopanda.gPlusAnalytics.dataStore;

import com.google.appengine.api.datastore.Entity;

/**
 * google Datastore 共通操作クラス
 * @author LeoPanda
 *
 */
public class DataStoreEntity {
  
  Entity entity;
  
  public DataStoreEntity(DataStoreProperty property){
    this.entity = new Entity(property.getName());
  }

  public DataStoreEntity(Entity entity){
    this.entity = entity;
  }
  
  /**
   * エンティティを返す
   * @return
   */
  public Entity getEntity(){
    return this.entity;
  }
    
  /**
   * entityのプロパティ値を返す
   * @param property
   * @return
   */
  public Object getProperty(DataStoreProperty property){
    return entity.getProperty(property.getName());
  }
  
  /**
   * entityにプロパティ値をセットする
   * @param property
   * @param value
   */
  public void setProperty(DataStoreProperty property,Object value){
    entity.setProperty(property.getName(), value);
  }
  
}
