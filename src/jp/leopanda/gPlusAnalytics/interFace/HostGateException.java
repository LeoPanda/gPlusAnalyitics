package jp.leopanda.gPlusAnalytics.interFace;

import java.io.Serializable;
import java.lang.Exception;

@SuppressWarnings("serial")
public class HostGateException extends Exception implements Serializable {
  public HostGateException() {
    super();
  }

  private String statusCode;

  public HostGateException(String statusCode) {
    super();
    this.statusCode = statusCode;
  }

  public String getStatus() {
    return this.statusCode;
  }

}
