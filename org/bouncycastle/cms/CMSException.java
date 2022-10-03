package org.bouncycastle.cms;

public class CMSException extends Exception {
  Exception e;
  
  public CMSException(String paramString) {
    super(paramString);
  }
  
  public CMSException(String paramString, Exception paramException) {
    super(paramString);
    this.e = paramException;
  }
  
  public Exception getUnderlyingException() {
    return this.e;
  }
  
  public Throwable getCause() {
    return this.e;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */