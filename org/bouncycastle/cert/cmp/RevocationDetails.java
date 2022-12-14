package org.bouncycastle.cert.cmp;

import java.math.BigInteger;
import org.bouncycastle.asn1.cmp.RevDetails;
import org.bouncycastle.asn1.x500.X500Name;

public class RevocationDetails {
  private RevDetails revDetails;
  
  public RevocationDetails(RevDetails paramRevDetails) {
    this.revDetails = paramRevDetails;
  }
  
  public X500Name getSubject() {
    return this.revDetails.getCertDetails().getSubject();
  }
  
  public X500Name getIssuer() {
    return this.revDetails.getCertDetails().getIssuer();
  }
  
  public BigInteger getSerialNumber() {
    return this.revDetails.getCertDetails().getSerialNumber().getValue();
  }
  
  public RevDetails toASN1Structure() {
    return this.revDetails;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\cmp\RevocationDetails.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */