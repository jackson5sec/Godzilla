package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;

public interface TlsHandshakeHash extends Digest {
  void init(TlsContext paramTlsContext);
  
  TlsHandshakeHash notifyPRFDetermined();
  
  void trackHashAlgorithm(short paramShort);
  
  void sealHashAlgorithms();
  
  TlsHandshakeHash stopTracking();
  
  Digest forkPRFHash();
  
  byte[] getFinalHash(short paramShort);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsHandshakeHash.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */