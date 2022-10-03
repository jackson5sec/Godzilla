package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class SM2KeyExchangePublicParameters implements CipherParameters {
  private ECPublicKeyParameters staticPublicKey;
  
  private ECPublicKeyParameters ephemeralPublicKey;
  
  public SM2KeyExchangePublicParameters(ECPublicKeyParameters paramECPublicKeyParameters1, ECPublicKeyParameters paramECPublicKeyParameters2) {
    if (paramECPublicKeyParameters1 == null)
      throw new NullPointerException("staticPublicKey cannot be null"); 
    if (paramECPublicKeyParameters2 == null)
      throw new NullPointerException("ephemeralPublicKey cannot be null"); 
    if (!paramECPublicKeyParameters1.getParameters().equals(paramECPublicKeyParameters2.getParameters()))
      throw new IllegalArgumentException("Static and ephemeral public keys have different domain parameters"); 
    this.staticPublicKey = paramECPublicKeyParameters1;
    this.ephemeralPublicKey = paramECPublicKeyParameters2;
  }
  
  public ECPublicKeyParameters getStaticPublicKey() {
    return this.staticPublicKey;
  }
  
  public ECPublicKeyParameters getEphemeralPublicKey() {
    return this.ephemeralPublicKey;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\SM2KeyExchangePublicParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */