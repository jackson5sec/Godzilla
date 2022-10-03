package org.bouncycastle.crypto.prng;

public interface RandomGenerator {
  void addSeedMaterial(byte[] paramArrayOfbyte);
  
  void addSeedMaterial(long paramLong);
  
  void nextBytes(byte[] paramArrayOfbyte);
  
  void nextBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\prng\RandomGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */