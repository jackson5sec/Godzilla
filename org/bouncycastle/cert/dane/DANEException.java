package org.bouncycastle.cert.dane;

public class DANEException extends Exception {
  private Throwable cause;
  
  public DANEException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public DANEException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\dane\DANEException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */