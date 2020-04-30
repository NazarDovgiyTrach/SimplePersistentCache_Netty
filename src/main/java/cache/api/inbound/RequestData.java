package cache.api.inbound;

import java.io.Serializable;

public class RequestData implements Serializable {
  private String action;
  private String stringValue;
  private byte[] binaryValue;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public byte[] getBinaryValue() {
    return binaryValue;
  }

  public void setBinaryValue(byte[] binaryValue) {
    this.binaryValue = binaryValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public String getStringValue() {
    return stringValue;
  }
}
