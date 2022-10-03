package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;

class TlsClientContextImpl extends AbstractTlsContext implements TlsClientContext {
  TlsClientContextImpl(SecureRandom paramSecureRandom, SecurityParameters paramSecurityParameters) {
    super(paramSecureRandom, paramSecurityParameters);
  }
  
  public boolean isServer() {
    return false;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsClientContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */