package org.bouncycastle.operator;

public class RuntimeOperatorException extends RuntimeException {
  private Throwable cause;
  
  public RuntimeOperatorException(String paramString) {
    super(paramString);
  }
  
  public RuntimeOperatorException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\RuntimeOperatorException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */