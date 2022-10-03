package org.bouncycastle.crypto;

public interface SignerWithRecovery extends Signer {
  boolean hasFullMessage();
  
  byte[] getRecoveredMessage();
  
  void updateWithRecoveredMessage(byte[] paramArrayOfbyte) throws InvalidCipherTextException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\SignerWithRecovery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */