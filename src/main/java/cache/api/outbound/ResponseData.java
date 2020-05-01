package cache.api.outbound;

import java.io.Serializable;

public class ResponseData implements Serializable {
  private String status;
  private String message;
  private String stringValue;
  private byte[] binaryValue;

  public ResponseData(String status) {
    this.status = status;
  }

  public ResponseData(String status, String message) {
    this.status = status;
    this.message = message;
  }

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
