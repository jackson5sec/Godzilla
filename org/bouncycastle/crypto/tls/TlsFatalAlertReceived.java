package org.bouncycastle.crypto.tls;

public class TlsFatalAlertReceived extends TlsException {
  protected short alertDescription;
  
  public TlsFatalAlertReceived(short paramShort) {
    super(AlertDescription.getText(paramShort), null);
    this.alertDescription = paramShort;
  }
  
  public short getAlertDescription() {
    return this.alertDescription;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsFatalAlertReceived.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */