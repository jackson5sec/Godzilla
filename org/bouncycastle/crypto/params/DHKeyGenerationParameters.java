package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class DHKeyGenerationParameters extends KeyGenerationParameters {
  private DHParameters params;
  
  public DHKeyGenerationParameters(SecureRandom paramSecureRandom, DHParameters paramDHParameters) {
    super(paramSecureRandom, getStrength(paramDHParameters));
    this.params = paramDHParameters;
  }
  
  public DHParameters getParameters() {
    return this.params;
  }
  
  static int getStrength(DHParameters paramDHParameters) {
    return (paramDHParameters.getL() != 0) ? paramDHParameters.getL() : paramDHParameters.getP().bitLength();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\DHKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */