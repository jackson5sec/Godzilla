package org.bouncycastle.cms;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface KEKRecipient extends Recipient {
  RecipientOperator getRecipientOperator(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, byte[] paramArrayOfbyte) throws CMSException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\KEKRecipient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */