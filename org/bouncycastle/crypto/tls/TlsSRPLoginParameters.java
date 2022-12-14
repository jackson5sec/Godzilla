package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class TlsSRPLoginParameters {
  protected SRP6GroupParameters group;
  
  protected BigInteger verifier;
  
  protected byte[] salt;
  
  public TlsSRPLoginParameters(SRP6GroupParameters paramSRP6GroupParameters, BigInteger paramBigInteger, byte[] paramArrayOfbyte) {
    this.group = paramSRP6GroupParameters;
    this.verifier = paramBigInteger;
    this.salt = paramArrayOfbyte;
  }
  
  public SRP6GroupParameters getGroup() {
    return this.group;
  }
  
  public byte[] getSalt() {
    return this.salt;
  }
  
  public BigInteger getVerifier() {
    return this.verifier;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsSRPLoginParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */