package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class SigningCertificateV2 extends ASN1Object {
  ASN1Sequence certs;
  
  ASN1Sequence policies;
  
  public static SigningCertificateV2 getInstance(Object paramObject) {
    return (paramObject == null || paramObject instanceof SigningCertificateV2) ? (SigningCertificateV2)paramObject : ((paramObject instanceof ASN1Sequence) ? new SigningCertificateV2((ASN1Sequence)paramObject) : null);
  }
  
  private SigningCertificateV2(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() < 1 || paramASN1Sequence.size() > 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size()); 
    this.certs = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.policies = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1)); 
  }
  
  public SigningCertificateV2(ESSCertIDv2 paramESSCertIDv2) {
    this.certs = (ASN1Sequence)new DERSequence((ASN1Encodable)paramESSCertIDv2);
  }
  
  public SigningCertificateV2(ESSCertIDv2[] paramArrayOfESSCertIDv2) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    for (byte b = 0; b < paramArrayOfESSCertIDv2.length; b++)
      aSN1EncodableVector.add((ASN1Encodable)paramArrayOfESSCertIDv2[b]); 
    this.certs = (ASN1Sequence)new DERSequence(aSN1EncodableVector);
  }
  
  public SigningCertificateV2(ESSCertIDv2[] paramArrayOfESSCertIDv2, PolicyInformation[] paramArrayOfPolicyInformation) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    byte b;
    for (b = 0; b < paramArrayOfESSCertIDv2.length; b++)
      aSN1EncodableVector.add((ASN1Encodable)paramArrayOfESSCertIDv2[b]); 
    this.certs = (ASN1Sequence)new DERSequence(aSN1EncodableVector);
    if (paramArrayOfPolicyInformation != null) {
      aSN1EncodableVector = new ASN1EncodableVector();
      for (b = 0; b < paramArrayOfPolicyInformation.length; b++)
        aSN1EncodableVector.add((ASN1Encodable)paramArrayOfPolicyInformation[b]); 
      this.policies = (ASN1Sequence)new DERSequence(aSN1EncodableVector);
    } 
  }
  
  public ESSCertIDv2[] getCerts() {
    ESSCertIDv2[] arrayOfESSCertIDv2 = new ESSCertIDv2[this.certs.size()];
    for (byte b = 0; b != this.certs.size(); b++)
      arrayOfESSCertIDv2[b] = ESSCertIDv2.getInstance(this.certs.getObjectAt(b)); 
    return arrayOfESSCertIDv2;
  }
  
  public PolicyInformation[] getPolicies() {
    if (this.policies == null)
      return null; 
    PolicyInformation[] arrayOfPolicyInformation = new PolicyInformation[this.policies.size()];
    for (byte b = 0; b != this.policies.size(); b++)
      arrayOfPolicyInformation[b] = PolicyInformation.getInstance(this.policies.getObjectAt(b)); 
    return arrayOfPolicyInformation;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    aSN1EncodableVector.add((ASN1Encodable)this.certs);
    if (this.policies != null)
      aSN1EncodableVector.add((ASN1Encodable)this.policies); 
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\ess\SigningCertificateV2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */