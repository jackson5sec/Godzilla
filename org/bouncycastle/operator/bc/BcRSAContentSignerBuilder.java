package org.bouncycastle.operator.bc;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.operator.OperatorCreationException;

public class BcRSAContentSignerBuilder extends BcContentSignerBuilder {
  public BcRSAContentSignerBuilder(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2) {
    super(paramAlgorithmIdentifier1, paramAlgorithmIdentifier2);
  }
  
  protected Signer createSigner(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2) throws OperatorCreationException {
    ExtendedDigest extendedDigest = this.digestProvider.get(paramAlgorithmIdentifier2);
    return (Signer)new RSADigestSigner((Digest)extendedDigest);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\bc\BcRSAContentSignerBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */