package org.bouncycastle.cert.crmf;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.InputDecryptor;

public interface ValueDecryptorGenerator {
  InputDecryptor getValueDecryptor(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, byte[] paramArrayOfbyte) throws CRMFException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\crmf\ValueDecryptorGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */