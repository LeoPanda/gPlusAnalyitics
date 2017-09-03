package jp.leopanda.gPlusAnalytics.dataStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

import com.google.appengine.api.datastore.Blob;
import com.google.gson.Gson;

/**
 * Blobエンティティ用オブジェクトシリアライザ
 * 
 * @author LeoPanda
 *
 */
public abstract class Serializer<T> {
  ByteArrayOutputStream bos;
  ObjectOutputStream oos;
  ByteArrayInputStream bis;
  ObjectInputStream ois;
  Gson gson = new Gson();
  Class<T> clazz;

  /**
   * エンコード用コンストラクタ
   */
  public Serializer() {
  }

  /**
   * デコード用コンストラクタ
   * 
   * @param clazz
   */
  public Serializer(Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * ジェネリック型のオブジェクトアイテムをJSONに変換した後 Blobへシリアライズする
   * 
   * @param item
   * @return
   * @throws HostGateException
   */
  public Blob encode(T item) throws HostGateException {
    String json = gson.toJson(item);
    return serialize(json);
  }

  /**
   * Blobシリアライザ
   * 
   * @param object
   * @return
   * @throws HostGateException
   */
  private Blob serialize(Object object) throws HostGateException {
    bos = new ByteArrayOutputStream();
    try {
      oos = new ObjectOutputStream(bos);
    } catch (IOException e) {
      throw new HostGateException(e.toString());
    }
    try {
      oos.writeObject(object);
    } catch (IOException e) {
      throw new HostGateException(e.toString());
    }
    return new Blob(bos.toByteArray());
  }

  /**
   * BlobへシリアライズされたJSONデータをジェネリック型のリストオブジェクトとして取り出す
   * 
   * @param blob
   * @return
   * @throws HostGateException
   */
  public List<T> decodeAsList(Blob blob) throws HostGateException {
    String jsonItem = (String) deSerialize(blob);
    ParameterizedType type = new ListParameterizedType(this.clazz);
    List<T> items = gson.fromJson(jsonItem, type);
    return items;
  }

  /**
   * Blobオブジェクトのデシリアライズ
   * 
   * @param blob
   * @return
   * @throws HostGateException
   */
  private Object deSerialize(Blob blob) throws HostGateException {
    bis = new ByteArrayInputStream(blob.getBytes());
    try {
      ois = new ObjectInputStream(bis);
    } catch (IOException e) {
      throw new HostGateException(e.toString());
    }
    Object result = null;
    try {
      result = (Object) ois.readObject();
    } catch (ClassNotFoundException | IOException e) {
      throw new HostGateException(e.toString());
    }
    return result;
  }

  /**
   * Gson fromJson リストクラスへのデコードタイプ指定用クラス
   * 
   * @author LeoPanda
   *
   */
  private class ListParameterizedType implements ParameterizedType {
    private Class<T> clazz;

    private ListParameterizedType(Class<T> clazz) {
      this.clazz = clazz;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[] { clazz };
    }

    @Override
    public Type getRawType() {
      return Collection.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }

  }

  /*
   * 以下、削除予定旧メソッド
   */
  @Deprecated
  public PlusActivity decodeAsPlusActivity(Blob blob) throws HostGateException {
    return (PlusActivity) deSerialize(blob);
  }

  @SuppressWarnings("unchecked")
  @Deprecated
  public List<PlusPeople> decodeAsPlusOners(Blob blob) throws HostGateException {
    return (List<PlusPeople>) deSerialize(blob);
  }

}
