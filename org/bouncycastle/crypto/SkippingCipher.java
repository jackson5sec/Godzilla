package org.bouncycastle.crypto;

public interface SkippingCipher {
  long skip(long paramLong);
  
  long seekTo(long paramLong);
  
  long getPosition();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\SkippingCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */