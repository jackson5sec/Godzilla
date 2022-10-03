package org.bouncycastle.cms.jcajce;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

interface KeyMaterialGenerator {
  byte[] generateKDFMaterial(AlgorithmIdentifier paramAlgorithmIdentifier, int paramInt, byte[] paramArrayOfbyte);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\jcajce\KeyMaterialGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */