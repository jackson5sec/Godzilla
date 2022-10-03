package org.bouncycastle.crypto.tls;

public abstract class ServerOnlyTlsAuthentication implements TlsAuthentication {
  public final TlsCredentials getClientCredentials(CertificateRequest paramCertificateRequest) {
    return null;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\ServerOnlyTlsAuthentication.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */