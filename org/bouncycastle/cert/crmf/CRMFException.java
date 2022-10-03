package org.bouncycastle.cert.crmf;

public class CRMFException extends Exception {
  private Throwable cause;
  
  public CRMFException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\crmf\CRMFException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */