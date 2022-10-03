package org.bouncycastle.crypto.modes.gcm;

public interface GCMExponentiator {
  void init(byte[] paramArrayOfbyte);
  
  void exponentiateX(long paramLong, byte[] paramArrayOfbyte);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\modes\gcm\GCMExponentiator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */