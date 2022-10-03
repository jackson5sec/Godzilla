package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;

public class SMIMECapabilitiesAttribute extends Attribute {
  public SMIMECapabilitiesAttribute(SMIMECapabilityVector paramSMIMECapabilityVector) {
    super(SMIMEAttributes.smimeCapabilities, (ASN1Set)new DERSet((ASN1Encodable)new DERSequence(paramSMIMECapabilityVector.toASN1EncodableVector())));
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\smime\SMIMECapabilitiesAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */