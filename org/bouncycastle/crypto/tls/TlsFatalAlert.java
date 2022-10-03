package org.bouncycastle.crypto.tls;

public class TlsFatalAlert extends TlsException {
  protected short alertDescription;
  
  public TlsFatalAlert(short paramShort) {
    this(paramShort, null);
  }
  
  public TlsFatalAlert(short paramShort, Throwable paramThrowable) {
    super(AlertDescription.getText(paramShort), paramThrowable);
    this.alertDescription = paramShort;
  }
  
  public short getAlertDescription() {
    return this.alertDescription;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsFatalAlert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */