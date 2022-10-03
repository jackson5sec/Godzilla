package org.bouncycastle.crypto.tls;

public interface TlsPSKIdentityManager {
  byte[] getHint();
  
  byte[] getPSK(byte[] paramArrayOfbyte);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsPSKIdentityManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */