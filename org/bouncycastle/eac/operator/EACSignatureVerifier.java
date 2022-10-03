package org.bouncycastle.eac.operator;

import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface EACSignatureVerifier {
  ASN1ObjectIdentifier getUsageIdentifier();
  
  OutputStream getOutputStream();
  
  boolean verify(byte[] paramArrayOfbyte);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\operator\EACSignatureVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */