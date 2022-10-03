package org.bouncycastle.crypto.params;

public class IESWithCipherParameters extends IESParameters {
  private int cipherKeySize;
  
  public IESWithCipherParameters(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2) {
    super(paramArrayOfbyte1, paramArrayOfbyte2, paramInt1);
    this.cipherKeySize = paramInt2;
  }
  
  public int getCipherKeySize() {
    return this.cipherKeySize;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\IESWithCipherParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */