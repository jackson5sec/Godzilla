package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface SMIMEAttributes {
  public static final ASN1ObjectIdentifier smimeCapabilities = PKCSObjectIdentifiers.pkcs_9_at_smimeCapabilities;
  
  public static final ASN1ObjectIdentifier encrypKeyPref = PKCSObjectIdentifiers.id_aa_encrypKeyPref;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\smime\SMIMEAttributes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */