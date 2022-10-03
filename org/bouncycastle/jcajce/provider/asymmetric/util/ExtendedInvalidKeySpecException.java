package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.security.spec.InvalidKeySpecException;

public class ExtendedInvalidKeySpecException extends InvalidKeySpecException {
  private Throwable cause;
  
  public ExtendedInvalidKeySpecException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\asymmetri\\util\ExtendedInvalidKeySpecException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */