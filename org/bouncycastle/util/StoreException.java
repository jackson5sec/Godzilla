package org.bouncycastle.util;

public class StoreException extends RuntimeException {
  private Throwable _e;
  
  public StoreException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._e = paramThrowable;
  }
  
  public Throwable getCause() {
    return this._e;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\StoreException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */