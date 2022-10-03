package org.bouncycastle.operator;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface AlgorithmNameFinder {
  boolean hasAlgorithmName(ASN1ObjectIdentifier paramASN1ObjectIdentifier);
  
  String getAlgorithmName(ASN1ObjectIdentifier paramASN1ObjectIdentifier);
  
  String getAlgorithmName(AlgorithmIdentifier paramAlgorithmIdentifier);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\AlgorithmNameFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */