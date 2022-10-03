package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;

class ExtCertificateEncodingException extends CertificateEncodingException {
  Throwable cause;
  
  ExtCertificateEncodingException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x509\ExtCertificateEncodingException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */