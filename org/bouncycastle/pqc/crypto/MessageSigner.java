package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface MessageSigner {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters);
  
  byte[] generateSignature(byte[] paramArrayOfbyte);
  
  boolean verifySignature(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\MessageSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */