package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithRandom implements CipherParameters {
  private SecureRandom random;
  
  private CipherParameters parameters;
  
  public ParametersWithRandom(CipherParameters paramCipherParameters, SecureRandom paramSecureRandom) {
    this.random = paramSecureRandom;
    this.parameters = paramCipherParameters;
  }
  
  public ParametersWithRandom(CipherParameters paramCipherParameters) {
    this(paramCipherParameters, new SecureRandom());
  }
  
  public SecureRandom getRandom() {
    return this.random;
  }
  
  public CipherParameters getParameters() {
    return this.parameters;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ParametersWithRandom.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */