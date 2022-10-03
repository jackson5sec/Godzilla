package org.bouncycastle.crypto;

public class CryptoException extends Exception {
  private Throwable cause;
  
  public CryptoException() {}
  
  public CryptoException(String paramString) {
    super(paramString);
  }
  
  public CryptoException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\CryptoException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */