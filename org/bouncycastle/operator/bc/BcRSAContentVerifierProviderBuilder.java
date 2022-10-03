package org.bouncycastle.operator.bc;

import java.io.IOException;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;

public class BcRSAContentVerifierProviderBuilder extends BcContentVerifierProviderBuilder {
  private DigestAlgorithmIdentifierFinder digestAlgorithmFinder;
  
  public BcRSAContentVerifierProviderBuilder(DigestAlgorithmIdentifierFinder paramDigestAlgorithmIdentifierFinder) {
    this.digestAlgorithmFinder = paramDigestAlgorithmIdentifierFinder;
  }
  
  protected Signer createSigner(AlgorithmIdentifier paramAlgorithmIdentifier) throws OperatorCreationException {
    AlgorithmIdentifier algorithmIdentifier = this.digestAlgorithmFinder.find(paramAlgorithmIdentifier);
    ExtendedDigest extendedDigest = this.digestProvider.get(algorithmIdentifier);
    return (Signer)new RSADigestSigner((Digest)extendedDigest);
  }
  
  protected AsymmetricKeyParameter extractKeyParameters(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
    return PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\bc\BcRSAContentVerifierProviderBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */