package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class RainbowKeyGenerationParameters extends KeyGenerationParameters {
  private RainbowParameters params;
  
  public RainbowKeyGenerationParameters(SecureRandom paramSecureRandom, RainbowParameters paramRainbowParameters) {
    super(paramSecureRandom, paramRainbowParameters.getVi()[(paramRainbowParameters.getVi()).length - 1] - paramRainbowParameters.getVi()[0]);
    this.params = paramRainbowParameters;
  }
  
  public RainbowParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\rainbow\RainbowKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */