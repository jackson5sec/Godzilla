package org.bouncycastle.crypto.io;

import java.io.IOException;

public class CipherIOException extends IOException {
  private static final long serialVersionUID = 1L;
  
  private final Throwable cause;
  
  public CipherIOException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\io\CipherIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */