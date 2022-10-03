package org.bouncycastle.eac;

public class EACException extends Exception {
  private Throwable cause;
  
  public EACException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public EACException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\EACException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */