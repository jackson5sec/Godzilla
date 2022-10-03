package org.bouncycastle.crypto.tls;

import java.io.IOException;

public interface TlsCipherFactory {
  TlsCipher createCipher(TlsContext paramTlsContext, int paramInt1, int paramInt2) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsCipherFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */