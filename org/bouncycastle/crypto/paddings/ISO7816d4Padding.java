package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class ISO7816d4Padding implements BlockCipherPadding {
  public void init(SecureRandom paramSecureRandom) throws IllegalArgumentException {}
  
  public String getPaddingName() {
    return "ISO7816-4";
  }
  
  public int addPadding(byte[] paramArrayOfbyte, int paramInt) {
    int i = paramArrayOfbyte.length - paramInt;
    paramArrayOfbyte[paramInt] = Byte.MIN_VALUE;
    while (++paramInt < paramArrayOfbyte.length) {
      paramArrayOfbyte[paramInt] = 0;
      paramInt++;
    } 
    return i;
  }
  
  public int padCount(byte[] paramArrayOfbyte) throws InvalidCipherTextException {
    int i;
    for (i = paramArrayOfbyte.length - 1; i > 0 && paramArrayOfbyte[i] == 0; i--);
    if (paramArrayOfbyte[i] != Byte.MIN_VALUE)
      throw new InvalidCipherTextException("pad block corrupted"); 
    return paramArrayOfbyte.length - i;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\paddings\ISO7816d4Padding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */