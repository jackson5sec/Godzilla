package org.bouncycastle.operator;

import java.io.OutputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestCalculator {
  AlgorithmIdentifier getAlgorithmIdentifier();
  
  OutputStream getOutputStream();
  
  byte[] getDigest();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\DigestCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */