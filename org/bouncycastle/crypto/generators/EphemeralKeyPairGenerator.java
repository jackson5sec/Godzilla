package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.EphemeralKeyPair;
import org.bouncycastle.crypto.KeyEncoder;

public class EphemeralKeyPairGenerator {
  private AsymmetricCipherKeyPairGenerator gen;
  
  private KeyEncoder keyEncoder;
  
  public EphemeralKeyPairGenerator(AsymmetricCipherKeyPairGenerator paramAsymmetricCipherKeyPairGenerator, KeyEncoder paramKeyEncoder) {
    this.gen = paramAsymmetricCipherKeyPairGenerator;
    this.keyEncoder = paramKeyEncoder;
  }
  
  public EphemeralKeyPair generate() {
    AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.gen.generateKeyPair();
    return new EphemeralKeyPair(asymmetricCipherKeyPair, this.keyEncoder);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\generators\EphemeralKeyPairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */