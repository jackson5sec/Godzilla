package org.bouncycastle.crypto.prng;

public interface EntropySource {
  boolean isPredictionResistant();
  
  byte[] getEntropy();
  
  int entropySize();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\prng\EntropySource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */