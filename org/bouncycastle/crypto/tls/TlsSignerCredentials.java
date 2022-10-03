package org.bouncycastle.crypto.tls;

import java.io.IOException;

public interface TlsSignerCredentials extends TlsCredentials {
  byte[] generateCertificateSignature(byte[] paramArrayOfbyte) throws IOException;
  
  SignatureAndHashAlgorithm getSignatureAndHashAlgorithm();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsSignerCredentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */