package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class ZeroBytePadding implements BlockCipherPadding {
  public void init(SecureRandom paramSecureRandom) throws IllegalArgumentException {}
  
  public String getPaddingName() {
    return "ZeroByte";
  }
  
  public int addPadding(byte[] paramArrayOfbyte, int paramInt) {
    int i = paramArrayOfbyte.length - paramInt;
    while (paramInt < paramArrayOfbyte.length) {
      paramArrayOfbyte[paramInt] = 0;
      paramInt++;
    } 
    return i;
  }
  
  public int padCount(byte[] paramArrayOfbyte) throws InvalidCipherTextException {
    int i;
    for (i = paramArrayOfbyte.length; i > 0 && paramArrayOfbyte[i - 1] == 0; i--);
    return paramArrayOfbyte.length - i;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\paddings\ZeroBytePadding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */