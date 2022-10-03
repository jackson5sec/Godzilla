package org.bouncycastle.cert;

public class CertRuntimeException extends RuntimeException {
  private Throwable cause;
  
  public CertRuntimeException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\CertRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */