package org.bouncycastle.util.encoders;

public class DecoderException extends IllegalStateException {
  private Throwable cause;
  
  DecoderException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\encoders\DecoderException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */