package org.bouncycastle.pkcs;

public class PKCSException extends Exception {
  private Throwable cause;
  
  public PKCSException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public PKCSException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pkcs\PKCSException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */