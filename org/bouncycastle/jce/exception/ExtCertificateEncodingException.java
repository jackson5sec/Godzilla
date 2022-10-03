package org.bouncycastle.jce.exception;

import java.security.cert.CertificateEncodingException;

public class ExtCertificateEncodingException extends CertificateEncodingException implements ExtException {
  private Throwable cause;
  
  public ExtCertificateEncodingException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\exception\ExtCertificateEncodingException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */