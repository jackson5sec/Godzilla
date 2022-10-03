package org.bouncycastle.cms.jcajce;

import java.io.IOException;
import org.bouncycastle.asn1.cms.ecc.ECCCMSSharedInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.Pack;

class RFC5753KeyMaterialGenerator implements KeyMaterialGenerator {
  public byte[] generateKDFMaterial(AlgorithmIdentifier paramAlgorithmIdentifier, int paramInt, byte[] paramArrayOfbyte) {
    ECCCMSSharedInfo eCCCMSSharedInfo = new ECCCMSSharedInfo(paramAlgorithmIdentifier, paramArrayOfbyte, Pack.intToBigEndian(paramInt));
    try {
      return eCCCMSSharedInfo.getEncoded("DER");
    } catch (IOException iOException) {
      throw new IllegalStateException("Unable to create KDF material: " + iOException);
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\jcajce\RFC5753KeyMaterialGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */