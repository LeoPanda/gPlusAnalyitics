package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

import java.util.logging.Logger;

/**
 * データストア上のソースアイテム操作クラス DataStoreには1セッション書き込みに1MBの上限制限があるので アイテムリストを複数のEntityに分割して保存する。
 * 
 * @author LeoPanda
 *
 */
public abstract class SourceItemHandler<T extends PlusItem> {

  int listLimit = 200;
  DatastoreService ds;
  SourceItemProperty entityProperty;
  EntityOperator sourceItemEntity;
  String actorId;
  String kind;
  Class<T> clazz;

  /**
   * コンストラクタ
   */
  public SourceItemHandler(String kind, DatastoreService ds, String actorId) {
    this.ds = ds;
    this.actorId = actorId;
    this.kind = kind;
    this.entityProperty = new SourceItemProperty(kind);
    sourceItemEntity = new EntityOperator(ds, entityProperty, actorId);
  }

  Logger logger = Logger.getLogger(this.kind + " SourceItem Handler");

  /**
   * エンコード時１エンティティに含むアイテムリストの最大値を設定する
   * 
   * @param listLimit
   */
  public void setListLimit(int listLimit) {
    this.listLimit = listLimit;
  }

  /**
   * SourceItemsをDataStoreから読み込む
   * 
   * @return SourceItems
   * @throws HostGateException
   */
  public List<T> getItems() throws HostGateException {
    List<T> items = new ArrayList<T>();
    List<Entity> entities = sourceItemEntity.getEntityAsList();
    if (!sourceItemEntity.isNew()) {
      for (Entity entity : entities) {
        List<T> itemsInEntity = decodeItems(entity);
        for (T item : itemsInEntity) {
          items.add(item);
        }
      }
    }
    return items;
  }

  public void setClass(Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * 複数のentityに分割されたアイテムリストを読み込む
   * 
   * @param entity
   * @return
   * @throws HostGateException
   */
  private List<T> decodeItems(Entity entity) throws HostGateException {
    sourceItemEntity.setEntity(entity);
    return new Serializer<T>(this.clazz) {
    }.decodeAsList((Blob) sourceItemEntity.getProperty(entityProperty.ITEMS));
  }

  /**
   * アイテムリストを複数に分割してDataStoreに書き込む
   * 
   * @param items
   * @throws HostGateException
   */
  public void putItems(List<T> items) throws HostGateException {
    sourceItemEntity.removeAllEntites();
    List<T> partItems = new ArrayList<T>();
    int counter = 0;
    int sequence = 0;
    for (T item : items) {
      partItems.add(item);
      counter++;
      if (counter > listLimit) {
        sequence = putItemsPart(sequence, partItems);
        partItems.clear();
        counter = 0;
      }
    }
    if (partItems.size() > 0) {
      sequence = putItemsPart(sequence, partItems);
    }
  }

  /**
   * 分割されたアイテムリストの１つを書き込む
   * 
   * @param sequence
   * @param items
   * @return
   * @throws HostGateException
   */
  private int putItemsPart(int sequence, List<T> items) throws HostGateException {
    sequence++;
    sourceItemEntity.newEntity();
    sourceItemEntity.setProperty(entityProperty.SEQUENCE, String.valueOf(sequence));
    sourceItemEntity.setProperty(entityProperty.ITEMS,
        new Serializer<List<T>>() {
        }.encode(items));
    sourceItemEntity.putEntity();
    return sequence;
  }
}
