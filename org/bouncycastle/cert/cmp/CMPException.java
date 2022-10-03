package org.bouncycastle.cert.cmp;

public class CMPException extends Exception {
  private Throwable cause;
  
  public CMPException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public CMPException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\cmp\CMPException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */