package org.bouncycastle.crypto.tls;

import java.io.IOException;

public class TlsException extends IOException {
  protected Throwable cause;
  
  public TlsException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */