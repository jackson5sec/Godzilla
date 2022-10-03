package org.bouncycastle.crypto.tls;

import java.io.IOException;

public class AbstractTlsCipherFactory implements TlsCipherFactory {
  public TlsCipher createCipher(TlsContext paramTlsContext, int paramInt1, int paramInt2) throws IOException {
    throw new TlsFatalAlert((short)80);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\AbstractTlsCipherFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */