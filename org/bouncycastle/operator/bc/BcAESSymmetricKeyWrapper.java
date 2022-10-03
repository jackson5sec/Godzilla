package org.bouncycastle.operator.bc;

import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;

public class BcAESSymmetricKeyWrapper extends BcSymmetricKeyWrapper {
  public BcAESSymmetricKeyWrapper(KeyParameter paramKeyParameter) {
    super(AESUtil.determineKeyEncAlg(paramKeyParameter), (Wrapper)new AESWrapEngine(), paramKeyParameter);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\bc\BcAESSymmetricKeyWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */