package org.bouncycastle.cert;

public class CertException extends Exception {
  private Throwable cause;
  
  public CertException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public CertException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\CertException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */