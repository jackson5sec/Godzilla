package org.bouncycastle.crypto;

public interface StreamCipher {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters) throws IllegalArgumentException;
  
  String getAlgorithmName();
  
  byte returnByte(byte paramByte);
  
  int processBytes(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3) throws DataLengthException;
  
  void reset();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\StreamCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */