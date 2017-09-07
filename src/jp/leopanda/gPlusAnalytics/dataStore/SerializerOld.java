package jp.leopanda.gPlusAnalytics.dataStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.google.appengine.api.datastore.Blob;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

/**
 * @author LeoPanda
 *
 */
@Deprecated
public class SerializerOld {
  ByteArrayOutputStream bos;
  ObjectOutputStream oos;
  ByteArrayInputStream bis;
  ObjectInputStream ois;

  // エンコード
  public Blob encode(PlusActivity item) throws HostGateException {
    return objectEncode(item);
  }

  public Blob encode(List<PlusPeople> items) throws HostGateException {
    return objectEncode(items);
  }

  private Blob objectEncode(Object object) throws HostGateException {
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

  // デコード
  public PlusActivity decodeAsPlusActivity(Blob blob) throws HostGateException {
    return (PlusActivity) decode(blob);
  }

  @SuppressWarnings("unchecked")
  public List<PlusPeople> decodeAsPlusOners(Blob blob) throws HostGateException {
    return (List<PlusPeople>) decode(blob);
  }

  private Object decode(Blob blob) throws HostGateException {
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

}
