package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

public class AnnotatedException extends Exception implements ExtException {
  private Throwable _underlyingException;
  
  public AnnotatedException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._underlyingException = paramThrowable;
  }
  
  public AnnotatedException(String paramString) {
    this(paramString, null);
  }
  
  Throwable getUnderlyingException() {
    return this._underlyingException;
  }
  
  public Throwable getCause() {
    return this._underlyingException;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\provider\AnnotatedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */