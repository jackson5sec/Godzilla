package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.DSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;

public class TlsDSSSigner extends TlsDSASigner {
  public boolean isValidPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
    return paramAsymmetricKeyParameter instanceof org.bouncycastle.crypto.params.DSAPublicKeyParameters;
  }
  
  protected DSA createDSAImpl(short paramShort) {
    return (DSA)new DSASigner((DSAKCalculator)new HMacDSAKCalculator(TlsUtils.createHash(paramShort)));
  }
  
  protected short getSignatureAlgorithm() {
    return 2;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsDSSSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */