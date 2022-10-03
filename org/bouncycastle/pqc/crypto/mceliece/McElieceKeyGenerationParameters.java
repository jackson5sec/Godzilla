package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class McElieceKeyGenerationParameters extends KeyGenerationParameters {
  private McElieceParameters params;
  
  public McElieceKeyGenerationParameters(SecureRandom paramSecureRandom, McElieceParameters paramMcElieceParameters) {
    super(paramSecureRandom, 256);
    this.params = paramMcElieceParameters;
  }
  
  public McElieceParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\mceliece\McElieceKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */