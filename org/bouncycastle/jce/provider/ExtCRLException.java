package org.bouncycastle.jce.provider;

import java.security.cert.CRLException;

class ExtCRLException extends CRLException {
  Throwable cause;
  
  ExtCRLException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\provider\ExtCRLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */