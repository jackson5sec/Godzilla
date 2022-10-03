package org.bouncycastle.cert.crmf;

public class CRMFRuntimeException extends RuntimeException {
  private Throwable cause;
  
  public CRMFRuntimeException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\crmf\CRMFRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */