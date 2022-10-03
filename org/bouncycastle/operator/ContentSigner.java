package org.bouncycastle.operator;

import java.io.OutputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface ContentSigner {
  AlgorithmIdentifier getAlgorithmIdentifier();
  
  OutputStream getOutputStream();
  
  byte[] getSignature();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\ContentSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */