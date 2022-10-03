package org.bouncycastle.crypto.tls;

import java.io.OutputStream;

public class TlsNullCompression implements TlsCompression {
  public OutputStream compress(OutputStream paramOutputStream) {
    return paramOutputStream;
  }
  
  public OutputStream decompress(OutputStream paramOutputStream) {
    return paramOutputStream;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsNullCompression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */