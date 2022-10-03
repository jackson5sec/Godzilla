package org.bouncycastle.operator;

import java.io.IOException;

public class OperatorStreamException extends IOException {
  private Throwable cause;
  
  public OperatorStreamException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\OperatorStreamException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */