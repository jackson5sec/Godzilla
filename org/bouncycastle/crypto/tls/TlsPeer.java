package org.bouncycastle.crypto.tls;

import java.io.IOException;

public interface TlsPeer {
  boolean shouldUseGMTUnixTime();
  
  void notifySecureRenegotiation(boolean paramBoolean) throws IOException;
  
  TlsCompression getCompression() throws IOException;
  
  TlsCipher getCipher() throws IOException;
  
  void notifyAlertRaised(short paramShort1, short paramShort2, String paramString, Throwable paramThrowable);
  
  void notifyAlertReceived(short paramShort1, short paramShort2);
  
  void notifyHandshakeComplete() throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsPeer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */