package org.bouncycastle.crypto;

public interface BlockCipher {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters) throws IllegalArgumentException;
  
  String getAlgorithmName();
  
  int getBlockSize();
  
  int processBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2) throws DataLengthException, IllegalStateException;
  
  void reset();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\BlockCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */