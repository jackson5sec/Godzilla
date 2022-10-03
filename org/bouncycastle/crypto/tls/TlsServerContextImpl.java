package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;

class TlsServerContextImpl extends AbstractTlsContext implements TlsServerContext {
  TlsServerContextImpl(SecureRandom paramSecureRandom, SecurityParameters paramSecurityParameters) {
    super(paramSecureRandom, paramSecurityParameters);
  }
  
  public boolean isServer() {
    return true;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsServerContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */