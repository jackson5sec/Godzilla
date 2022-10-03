package org.bouncycastle.crypto;

public interface AsymmetricCipherKeyPairGenerator {
  void init(KeyGenerationParameters paramKeyGenerationParameters);
  
  AsymmetricCipherKeyPair generateKeyPair();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\AsymmetricCipherKeyPairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */