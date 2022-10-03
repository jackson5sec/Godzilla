package org.bouncycastle.crypto.tls;

public class SupplementalDataEntry {
  protected int dataType;
  
  protected byte[] data;
  
  public SupplementalDataEntry(int paramInt, byte[] paramArrayOfbyte) {
    this.dataType = paramInt;
    this.data = paramArrayOfbyte;
  }
  
  public int getDataType() {
    return this.dataType;
  }
  
  public byte[] getData() {
    return this.data;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\SupplementalDataEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */