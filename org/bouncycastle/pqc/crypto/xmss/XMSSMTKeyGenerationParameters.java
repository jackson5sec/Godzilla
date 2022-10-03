package org.bouncycastle.pqc.crypto.xmss;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public final class XMSSMTKeyGenerationParameters extends KeyGenerationParameters {
  private final XMSSMTParameters xmssmtParameters;
  
  public XMSSMTKeyGenerationParameters(XMSSMTParameters paramXMSSMTParameters, SecureRandom paramSecureRandom) {
    super(paramSecureRandom, -1);
    this.xmssmtParameters = paramXMSSMTParameters;
  }
  
  public XMSSMTParameters getParameters() {
    return this.xmssmtParameters;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\xmss\XMSSMTKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */