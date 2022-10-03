package org.bouncycastle.operator;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface SecretKeySizeProvider {
  int getKeySize(AlgorithmIdentifier paramAlgorithmIdentifier);
  
  int getKeySize(ASN1ObjectIdentifier paramASN1ObjectIdentifier);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\SecretKeySizeProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */