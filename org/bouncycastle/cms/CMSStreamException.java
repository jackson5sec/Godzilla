package org.bouncycastle.cms;

import java.io.IOException;

public class CMSStreamException extends IOException {
  private final Throwable underlying = null;
  
  CMSStreamException(String paramString) {
    super(paramString);
  }
  
  CMSStreamException(String paramString, Throwable paramThrowable) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.underlying;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSStreamException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */