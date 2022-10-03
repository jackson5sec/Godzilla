package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;

public class ElGamalKeySpec implements KeySpec {
  private ElGamalParameterSpec spec;
  
  public ElGamalKeySpec(ElGamalParameterSpec paramElGamalParameterSpec) {
    this.spec = paramElGamalParameterSpec;
  }
  
  public ElGamalParameterSpec getParams() {
    return this.spec;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ElGamalKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */