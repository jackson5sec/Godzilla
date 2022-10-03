package org.bouncycastle.openssl;

import org.bouncycastle.operator.OperatorCreationException;

public interface PEMDecryptorProvider {
  PEMDecryptor get(String paramString) throws OperatorCreationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\openssl\PEMDecryptorProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */