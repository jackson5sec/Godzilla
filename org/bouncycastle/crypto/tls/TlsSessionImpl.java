package org.bouncycastle.crypto.tls;

import org.bouncycastle.util.Arrays;

class TlsSessionImpl implements TlsSession {
  final byte[] sessionID;
  
  SessionParameters sessionParameters;
  
  TlsSessionImpl(byte[] paramArrayOfbyte, SessionParameters paramSessionParameters) {
    if (paramArrayOfbyte == null)
      throw new IllegalArgumentException("'sessionID' cannot be null"); 
    if (paramArrayOfbyte.length < 1 || paramArrayOfbyte.length > 32)
      throw new IllegalArgumentException("'sessionID' must have length between 1 and 32 bytes, inclusive"); 
    this.sessionID = Arrays.clone(paramArrayOfbyte);
    this.sessionParameters = paramSessionParameters;
  }
  
  public synchronized SessionParameters exportSessionParameters() {
    return (this.sessionParameters == null) ? null : this.sessionParameters.copy();
  }
  
  public synchronized byte[] getSessionID() {
    return this.sessionID;
  }
  
  public synchronized void invalidate() {
    if (this.sessionParameters != null) {
      this.sessionParameters.clear();
      this.sessionParameters = null;
    } 
  }
  
  public synchronized boolean isResumable() {
    return (this.sessionParameters != null);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsSessionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */