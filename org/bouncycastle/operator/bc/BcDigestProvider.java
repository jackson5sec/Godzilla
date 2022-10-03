package org.bouncycastle.operator.bc;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.operator.OperatorCreationException;

public interface BcDigestProvider {
  ExtendedDigest get(AlgorithmIdentifier paramAlgorithmIdentifier) throws OperatorCreationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\bc\BcDigestProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */