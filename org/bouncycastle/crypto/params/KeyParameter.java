package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class KeyParameter implements CipherParameters {
  private byte[] key;
  
  public KeyParameter(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public KeyParameter(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.key = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, this.key, 0, paramInt2);
  }
  
  public byte[] getKey() {
    return this.key;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\KeyParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */