package org.bouncycastle.cms;

import org.bouncycastle.asn1.cms.RecipientInfo;
import org.bouncycastle.operator.GenericKey;

public interface RecipientInfoGenerator {
  RecipientInfo generate(GenericKey paramGenericKey) throws CMSException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\RecipientInfoGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */