package org.bouncycastle.asn1.eac;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;

public abstract class PublicKeyDataObject extends ASN1Object {
  public static PublicKeyDataObject getInstance(Object paramObject) {
    if (paramObject instanceof PublicKeyDataObject)
      return (PublicKeyDataObject)paramObject; 
    if (paramObject != null) {
      ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramObject);
      ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
      return (PublicKeyDataObject)(aSN1ObjectIdentifier.on(EACObjectIdentifiers.id_TA_ECDSA) ? new ECDSAPublicKey(aSN1Sequence) : new RSAPublicKey(aSN1Sequence));
    } 
    return null;
  }
  
  public abstract ASN1ObjectIdentifier getUsage();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\eac\PublicKeyDataObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */