package org.bouncycastle.operator.bc;

import org.bouncycastle.asn1.kisa.KISAObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

class SEEDUtil {
  static AlgorithmIdentifier determineKeyEncAlg() {
    return new AlgorithmIdentifier(KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\bc\SEEDUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */