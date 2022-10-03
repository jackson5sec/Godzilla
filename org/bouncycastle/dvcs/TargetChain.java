package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.TargetEtcChain;

public class TargetChain {
  private final TargetEtcChain certs;
  
  public TargetChain(TargetEtcChain paramTargetEtcChain) {
    this.certs = paramTargetEtcChain;
  }
  
  public TargetEtcChain toASN1Structure() {
    return this.certs;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\dvcs\TargetChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */