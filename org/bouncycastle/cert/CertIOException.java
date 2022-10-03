package org.bouncycastle.cert;

import java.io.IOException;

public class CertIOException extends IOException {
  private Throwable cause;
  
  public CertIOException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public CertIOException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\CertIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */