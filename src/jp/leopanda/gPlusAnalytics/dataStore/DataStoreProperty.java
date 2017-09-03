package jp.leopanda.gPlusAnalytics.dataStore;

import com.google.appengine.api.datastore.PropertyProjection;

/**
 * Google DataStore　Property共通インターフェース
 * @author LeoPanda
 *
 */
@Deprecated
public interface DataStoreProperty {
  
  public String getKind();
  public String getName();
  public Class<?> getClazz();
  public PropertyProjection getProjection();

}
