package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class KeyGenerationParameters {
  private SecureRandom random;
  
  private int strength;
  
  public KeyGenerationParameters(SecureRandom paramSecureRandom, int paramInt) {
    this.random = paramSecureRandom;
    this.strength = paramInt;
  }
  
  public SecureRandom getRandom() {
    return this.random;
  }
  
  public int getStrength() {
    return this.strength;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\KeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */