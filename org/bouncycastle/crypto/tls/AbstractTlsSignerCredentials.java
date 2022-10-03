package org.bouncycastle.crypto.tls;

public abstract class AbstractTlsSignerCredentials extends AbstractTlsCredentials implements TlsSignerCredentials {
  public SignatureAndHashAlgorithm getSignatureAndHashAlgorithm() {
    throw new IllegalStateException("TlsSignerCredentials implementation does not support (D)TLS 1.2+");
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\AbstractTlsSignerCredentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */