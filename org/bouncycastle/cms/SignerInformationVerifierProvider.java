package org.bouncycastle.cms;

import org.bouncycastle.operator.OperatorCreationException;

public interface SignerInformationVerifierProvider {
  SignerInformationVerifier get(SignerId paramSignerId) throws OperatorCreationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\SignerInformationVerifierProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */