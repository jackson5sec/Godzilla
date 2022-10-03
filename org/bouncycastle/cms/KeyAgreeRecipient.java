package org.bouncycastle.cms;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface KeyAgreeRecipient extends Recipient {
  RecipientOperator getRecipientOperator(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, SubjectPublicKeyInfo paramSubjectPublicKeyInfo, ASN1OctetString paramASN1OctetString, byte[] paramArrayOfbyte) throws CMSException;
  
  AlgorithmIdentifier getPrivateKeyAlgorithmIdentifier();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\KeyAgreeRecipient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */