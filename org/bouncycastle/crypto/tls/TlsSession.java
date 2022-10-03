package org.bouncycastle.crypto.tls;

public interface TlsSession {
  SessionParameters exportSessionParameters();
  
  byte[] getSessionID();
  
  void invalidate();
  
  boolean isResumable();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */