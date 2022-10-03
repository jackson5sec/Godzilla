package org.bouncycastle.crypto.tls;

public interface TlsPSKIdentity {
  void skipIdentityHint();
  
  void notifyIdentityHint(byte[] paramArrayOfbyte);
  
  byte[] getPSKIdentity();
  
  byte[] getPSK();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsPSKIdentity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */