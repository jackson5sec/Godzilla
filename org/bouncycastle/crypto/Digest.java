package org.bouncycastle.crypto;

public interface Digest {
  String getAlgorithmName();
  
  int getDigestSize();
  
  void update(byte paramByte);
  
  void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int doFinal(byte[] paramArrayOfbyte, int paramInt);
  
  void reset();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\Digest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */