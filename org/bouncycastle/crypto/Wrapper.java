package org.bouncycastle.crypto;

public interface Wrapper {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters);
  
  String getAlgorithmName();
  
  byte[] wrap(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  byte[] unwrap(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws InvalidCipherTextException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\Wrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */