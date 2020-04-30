package cache.api.outbound;

import java.io.Serializable;

public class ResponseData implements Serializable {
  private String status;
  private String stringValue;
  private byte[] binaryValue;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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
