package org.bouncycastle.x509.util;

public class StreamParsingException extends Exception {
  Throwable _e;
  
  public StreamParsingException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._e = paramThrowable;
  }
  
  public Throwable getCause() {
    return this._e;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x50\\util\StreamParsingException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */