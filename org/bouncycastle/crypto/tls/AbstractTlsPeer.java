package org.bouncycastle.crypto.tls;

import java.io.IOException;

public abstract class AbstractTlsPeer implements TlsPeer {
  public boolean shouldUseGMTUnixTime() {
    return false;
  }
  
  public void notifySecureRenegotiation(boolean paramBoolean) throws IOException {
    if (!paramBoolean)
      throw new TlsFatalAlert((short)40); 
  }
  
  public void notifyAlertRaised(short paramShort1, short paramShort2, String paramString, Throwable paramThrowable) {}
  
  public void notifyAlertReceived(short paramShort1, short paramShort2) {}
  
  public void notifyHandshakeComplete() throws IOException {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\AbstractTlsPeer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */