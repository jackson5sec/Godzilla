package org.bouncycastle.crypto.tls;

import java.io.EOFException;

public class TlsNoCloseNotifyException extends EOFException {
  public TlsNoCloseNotifyException() {
    super("No close_notify alert received before connection closed");
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsNoCloseNotifyException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */