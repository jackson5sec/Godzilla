package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class DSAKeyGenerationParameters extends KeyGenerationParameters {
  private DSAParameters params;
  
  public DSAKeyGenerationParameters(SecureRandom paramSecureRandom, DSAParameters paramDSAParameters) {
    super(paramSecureRandom, paramDSAParameters.getP().bitLength() - 1);
    this.params = paramDSAParameters;
  }
  
  public DSAParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\DSAKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */