package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public interface BlockCipherPadding {
  void init(SecureRandom paramSecureRandom) throws IllegalArgumentException;
  
  String getPaddingName();
  
  int addPadding(byte[] paramArrayOfbyte, int paramInt);
  
  int padCount(byte[] paramArrayOfbyte) throws InvalidCipherTextException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\paddings\BlockCipherPadding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */