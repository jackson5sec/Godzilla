package org.bouncycastle.crypto.tls;

import java.io.OutputStream;

public interface TlsCompression {
  OutputStream compress(OutputStream paramOutputStream);
  
  OutputStream decompress(OutputStream paramOutputStream);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsCompression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */