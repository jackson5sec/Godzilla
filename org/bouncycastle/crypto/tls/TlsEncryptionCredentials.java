package org.bouncycastle.crypto.tls;

import java.io.IOException;

public interface TlsEncryptionCredentials extends TlsCredentials {
  byte[] decryptPreMasterSecret(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsEncryptionCredentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */