package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.RandomGenerator;

public interface TlsContext {
  RandomGenerator getNonceRandomGenerator();
  
  SecureRandom getSecureRandom();
  
  SecurityParameters getSecurityParameters();
  
  boolean isServer();
  
  ProtocolVersion getClientVersion();
  
  ProtocolVersion getServerVersion();
  
  TlsSession getResumableSession();
  
  Object getUserObject();
  
  void setUserObject(Object paramObject);
  
  byte[] exportKeyingMaterial(String paramString, byte[] paramArrayOfbyte, int paramInt);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */